package com.hospital.boot.domain.hospital.model.service.impl;

import com.hospital.boot.app.hospital.dto.HospitalSearchRequest;
import com.hospital.boot.app.hospital.dto.HospitalWithDistance;
import com.hospital.boot.domain.hospital.model.vo.Hospital;
import com.hospital.boot.domain.hospital.model.service.HospitalService;
import com.hospital.boot.domain.hospital.model.service.DistanceService;
import com.hospital.boot.domain.hospital.model.mapper.HospitalMapper;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class HospitalServiceImpl implements HospitalService {

    private final HospitalMapper hMapper;
    private final DistanceService distanceService;

    // 비동기 처리를 위한 스레드 풀 (20개 동시 처리)
    private final ExecutorService executorService = Executors.newFixedThreadPool(20);

    @Override
    public List<Hospital> findHospitalByDepartments(List<String> departments) {
        return hMapper.findHospitalByDepartments(departments);
    }

    @Override
    public List<HospitalWithDistance> findHospitalByDepartmentsWithDistance(HospitalSearchRequest request) {
        long startTime = System.currentTimeMillis();
        log.info("===== 병원 검색 시작 =====");
        log.info("요청 정보 - 진료과: {}, 좌표: ({},{}), 거리필터: {}km, 정렬: {}",
                request.getDepartments(), request.getLatitude(), request.getLongitude(),
                request.getDistance(), request.getSortBy());

        List<Hospital> hospitals;

        // 1. 사용자 좌표로 지역구 판별
        if (request.getLatitude() != null && request.getLongitude() != null) {
            long districtStartTime = System.currentTimeMillis();

            String userDistrict = districtService.findClosestDistrict(
                    request.getLatitude(),
                    request.getLongitude()
            );

            long districtTime = System.currentTimeMillis() - districtStartTime;
            log.info("[1단계] 사용자 위치 지역구: {} (소요시간: {}ms)", userDistrict, districtTime);

            // 2. 진료과 + 지역구로 병원 조회 (DB 레벨 최적화)
            long dbStartTime = System.currentTimeMillis();

            hospitals = hMapper.findHospitalByDepartmentsAndDistrict(
                    request.getDepartments(),
                    userDistrict
            );

            long dbTime = System.currentTimeMillis() - dbStartTime;
            log.info("[2단계] DB 조회 완료 - 지역구 필터링 후 병원 수: {} (소요시간: {}ms)",
                    hospitals.size(), dbTime);
        } else {
            // GPS 정보 없으면 기존 방식
            long dbStartTime = System.currentTimeMillis();

            hospitals = hMapper.findHospitalByDepartments(request.getDepartments());

            long dbTime = System.currentTimeMillis() - dbStartTime;
            log.info("[2단계] DB 조회 완료 - 전체 병원 수: {} (소요시간: {}ms)",
                    hospitals.size(), dbTime);
        }

        // 3. 직선거리로 1차 필터링 (사용자 요청 거리의 80% 사용)
        // 직선거리 < 실거리 이므로, 요청 거리보다 작게 설정하여 API 호출 최소화
        int straightDistanceFilter;
        if (request.getDistance() != null) {
            straightDistanceFilter = (int)(request.getDistance() * 1000 * 0.8); // 80%로 설정
        } else {
            straightDistanceFilter = 4000; // 기본값 4km
        }

        log.info("[3단계] 직선거리 1차 필터링 시작 - {} 개 병원 (필터: {}km)",
                hospitals.size(), straightDistanceFilter / 1000.0);
        long straightFilterStartTime = System.currentTimeMillis();

        List<Hospital> nearbyHospitals = new ArrayList<>();

        for (Hospital hospital : hospitals) {
            try {
                double hospitalLat = Double.parseDouble(hospital.getLatitude());
                double hospitalLng = Double.parseDouble(hospital.getLongitude());

                int straightDistance = distanceService.calculateStraightDistance(
                        request.getLatitude(),
                        request.getLongitude(),
                        hospitalLat,
                        hospitalLng
                );

                if (straightDistance <= straightDistanceFilter) {
                    nearbyHospitals.add(hospital);
                }
            } catch (NumberFormatException e) {
                log.error("병원 {} 좌표 파싱 실패", hospital.getHospitalName());
            }
        }

        long straightFilterTime = System.currentTimeMillis() - straightFilterStartTime;
        log.info("[3단계] 직선거리 {}km 필터링 완료 - {} → {} 개 (소요시간: {}ms)",
                straightDistanceFilter / 1000.0, hospitals.size(), nearbyHospitals.size(), straightFilterTime);

        log.info("[4단계] OSRM API 거리 계산 시작 (비동기 병렬) - {} 개 병원 처리", nearbyHospitals.size());
        long apiTotalStartTime = System.currentTimeMillis();

        // 4. 비동기 병렬로 거리 계산
        List<CompletableFuture<HospitalWithDistance>> futures = nearbyHospitals.stream()
                .map(hospital -> CompletableFuture.supplyAsync(() -> {
                    try {
                        // 병원 좌표 파싱
                        double hospitalLat = Double.parseDouble(hospital.getLatitude());
                        double hospitalLng = Double.parseDouble(hospital.getLongitude());

                        // OSRM API로 실제 거리 계산
                        int[] result = distanceService.calculateDistance(
                                request.getLatitude(),
                                request.getLongitude(),
                                hospitalLat,
                                hospitalLng
                        );

                        Integer distance;
                        Integer duration;

                        if (result != null) {
                            distance = result[0];  // 미터
                            duration = result[1];  // 초
                        } else {
                            // API 실패시 직선거리로 fallback
                            distance = distanceService.calculateStraightDistance(
                                    request.getLatitude(),
                                    request.getLongitude(),
                                    hospitalLat,
                                    hospitalLng
                            );
                            duration = null;
                            log.debug("병원 {}에 대한 OSRM API 호출 실패, 직선거리로 대체: {}m",
                                    hospital.getHospitalName(), distance);
                        }

                        // 거리 필터링 (1km, 3km, 5km)
                        if (request.getDistance() != null) {
                            int maxDistanceInMeters = request.getDistance() * 1000;
                            if (distance != null && distance <= maxDistanceInMeters) {
                                return new HospitalWithDistance(hospital, distance, duration);
                            }
                        } else {
                            // 거리 필터가 없으면 모두 추가
                            return new HospitalWithDistance(hospital, distance, duration);
                        }

                        return null;

                    } catch (NumberFormatException e) {
                        log.error("병원 {} 좌표 파싱 실패: lat={}, lng={}",
                                hospital.getHospitalName(), hospital.getLatitude(), hospital.getLongitude());
                        return null;
                    } catch (Exception e) {
                        log.error("병원 {} 거리 계산 중 오류: {}", hospital.getHospitalName(), e.getMessage());
                        return null;
                    }
                }, executorService))
                .collect(Collectors.toList());

        // 모든 비동기 작업 완료 대기
        CompletableFuture<Void> allOf = CompletableFuture.allOf(
                futures.toArray(new CompletableFuture[0])
        );

        try {
            allOf.join(); // 모든 작업 완료 대기
        } catch (Exception e) {
            log.error("비동기 처리 중 오류 발생", e);
        }

        // 결과 수집
        List<HospitalWithDistance> hospitalsWithDistance = futures.stream()
                .map(CompletableFuture::join)
                .filter(result -> result != null)
                .collect(Collectors.toList());

        long apiTotalTime = System.currentTimeMillis() - apiTotalStartTime;
        int apiSuccessCount = (int) hospitalsWithDistance.stream().filter(h -> h.getDuration() != null).count();
        int apiFallbackCount = (int) hospitalsWithDistance.stream().filter(h -> h.getDuration() == null).count();

        log.info("[4단계] OSRM API 거리 계산 완료 - API 성공: {}, Fallback: {}, 총 소요시간: {}ms",
                apiSuccessCount, apiFallbackCount, apiTotalTime);

        log.info("[5단계] 실거리 필터링 후 병원 수: {}", hospitalsWithDistance.size());

        // 6. 거리순 정렬
        if ("distance".equalsIgnoreCase(request.getSortBy())) {
            long sortStartTime = System.currentTimeMillis();

            hospitalsWithDistance.sort(Comparator.comparing(
                    HospitalWithDistance::getDistance,
                    Comparator.nullsLast(Comparator.naturalOrder())
            ));

            long sortTime = System.currentTimeMillis() - sortStartTime;
            log.info("[6단계] 거리순 정렬 완료 (소요시간: {}ms)", sortTime);
        }

        long totalTime = System.currentTimeMillis() - startTime;
        log.info("===== 병원 검색 완료 - 총 소요시간: {}ms ({} 초) =====", totalTime, totalTime / 1000.0);

        return hospitalsWithDistance;
    }

    @Override
    public List<Hospital> findEmergencyHospital() {
        return hMapper.findEmergencyHospital();
    }
}
