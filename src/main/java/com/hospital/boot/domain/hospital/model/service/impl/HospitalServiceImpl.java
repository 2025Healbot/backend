package com.hospital.boot.domain.hospital.model.service.impl;

import com.hospital.boot.app.hospital.dto.HospitalSearchRequest;
import com.hospital.boot.app.hospital.dto.HospitalWithDistance;
import com.hospital.boot.domain.hospital.model.vo.Hospital;
import com.hospital.boot.domain.hospital.model.service.HospitalService;
import com.hospital.boot.domain.hospital.model.service.DistanceService;
import com.hospital.boot.domain.hospital.model.mapper.HospitalMapper;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

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
        List<Hospital> hospitals = hMapper.findHospitalByDepartments(request.getDepartments());

        int straightDistanceFilter;
        if (request.getDistance() != null) {
            straightDistanceFilter = (int)(request.getDistance() * 1000 * 0.8);
        } else {
            straightDistanceFilter = 4000;
        }

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
                // 좌표 파싱 실패 시 무시
            }
        }
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
                            distance = distanceService.calculateStraightDistance(
                                    request.getLatitude(),
                                    request.getLongitude(),
                                    hospitalLat,
                                    hospitalLng
                            );
                            duration = null;
                        }

                        if (request.getDistance() != null) {
                            int maxDistanceInMeters = request.getDistance() * 1000;
                            if (distance != null && distance <= maxDistanceInMeters) {
                                return new HospitalWithDistance(hospital, distance, duration);
                            }
                        } else {
                            return new HospitalWithDistance(hospital, distance, duration);
                        }

                        return null;

                    } catch (Exception e) {
                        return null;
                    }
                }, executorService))
                .collect(Collectors.toList());

        // 모든 비동기 작업 완료 대기
        CompletableFuture<Void> allOf = CompletableFuture.allOf(
                futures.toArray(new CompletableFuture[0])
        );

        try {
            allOf.join();
        } catch (Exception e) {
            // 비동기 처리 오류 무시
        }

        List<HospitalWithDistance> hospitalsWithDistance = futures.stream()
                .map(CompletableFuture::join)
                .filter(result -> result != null)
                .collect(Collectors.toList());

        if ("distance".equalsIgnoreCase(request.getSortBy())) {
            hospitalsWithDistance.sort(Comparator.comparing(
                    HospitalWithDistance::getDistance,
                    Comparator.nullsLast(Comparator.naturalOrder())
            ));
        }

        return hospitalsWithDistance;
    }

    @Override
    public List<Hospital> findEmergencyHospital() {
        return hMapper.findEmergencyHospital();
    }

    // 관리자용 CRUD 구현
    @Override
    public List<Hospital> getAllHospitals() {
        return hMapper.getAllHospitals();
    }

    @Override
    public void insertHospital(Hospital hospital) {
        hMapper.insertHospital(hospital);
    }

    @Override
    public void updateHospital(Hospital hospital) {
        hMapper.updateHospital(hospital);
    }

    @Override
    public void deleteHospital(String hospitalId) {
        hMapper.deleteHospital(hospitalId);
    }

    @Override
    public List<String> getDepartmentsByHospitalId(String hospitalId) {
        return hMapper.getDepartmentsByHospitalId(hospitalId);
    }
}
