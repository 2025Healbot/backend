package com.hospital.boot.app.admin.controller;

import com.hospital.boot.domain.admin.model.service.AdminService;
import com.hospital.boot.domain.member.model.vo.Member;
import com.hospital.boot.domain.notice.model.service.NoticeService;
import com.hospital.boot.domain.notice.model.vo.Notice;
import com.hospital.boot.domain.hospital.model.service.HospitalService;
import com.hospital.boot.domain.hospital.model.vo.Hospital;
import com.hospital.boot.app.admin.dto.HospitalDTO;
import com.hospital.boot.domain.accesslog.model.service.AccessLogService;
import com.hospital.boot.domain.diseases.model.service.DiseasesService;
import com.hospital.boot.domain.diseases.model.vo.Diseases;
import com.hospital.boot.common.util.CloudflareR2Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService aService;
    private final NoticeService nService;
    private final HospitalService hService;
    private final AccessLogService alService;
    private final DiseasesService dService;
    private final CloudflareR2Service r2Service;

    /**
     * 전체 회원 목록 조회
     * @return 회원 목록
     */
    @GetMapping("/members")
    public List<Member> getAllMembers() {
        return aService.getAllMembers();
    }

    /**
     * 회원 수정
     * @param memberId 수정할 회원 ID
     * @param member 수정할 회원 정보
     * @return 수정 성공 여부
     */
    @PutMapping("/members/{memberId}")
    public Map<String, Object> updateMember(@PathVariable String memberId, @RequestBody Member member) {
        Map<String, Object> response = new HashMap<>();

        member.setMemberId(memberId);
        int result = aService.updateMember(member);

        if (result > 0) {
            response.put("success", true);
            response.put("message", "회원 정보가 수정되었습니다.");
        } else {
            response.put("success", false);
            response.put("message", "회원 수정에 실패했습니다.");
        }

        return response;
    }

    /**
     * 회원 삭제
     * @param memberId 삭제할 회원 ID
     * @return 삭제 성공 여부
     */
    @DeleteMapping("/members/{memberId}")
    public Map<String, Object> deleteMember(@PathVariable String memberId) {
        Map<String, Object> response = new HashMap<>();

        int result = aService.deleteMember(memberId);

        if (result > 0) {
            response.put("success", true);
            response.put("message", "회원이 삭제되었습니다.");
        } else {
            response.put("success", false);
            response.put("message", "회원 삭제에 실패했습니다.");
        }

        return response;
    }

    /**
     * 전체 공지사항 목록 조회
     * @return 공지사항 목록
     */
    @GetMapping("/notices")
    public List<Notice> getAllNotices() {
        return nService.getAllNotices();
    }

    /**
     * 공지사항 생성
     * @param notice 공지사항 정보
     * @return 생성 성공 여부
     */
    @PostMapping("/notices")
    public Map<String, Object> createNotice(@RequestBody Notice notice) {
        Map<String, Object> response = new HashMap<>();

        System.out.print(notice);
        int result = nService.createNotice(notice);

        if (result > 0) {
            response.put("success", true);
            response.put("message", "공지사항이 등록되었습니다.");
        } else {
            response.put("success", false);
            response.put("message", "공지사항 등록에 실패했습니다.");
        }

        return response;
    }

    /**
     * 공지사항 수정
     * @param noticeId 공지사항 ID
     * @param notice 수정할 공지사항 정보
     * @return 수정 성공 여부
     */
    @PutMapping("/notices/{noticeId}")
    public Map<String, Object> updateNotice(@PathVariable int noticeId, @RequestBody Notice notice) {
        Map<String, Object> response = new HashMap<>();

        notice.setNoticeNo(noticeId);
        int result = nService.updateNotice(notice);

        if (result > 0) {
            response.put("success", true);
            response.put("message", "공지사항이 수정되었습니다.");
        } else {
            response.put("success", false);
            response.put("message", "공지사항 수정에 실패했습니다.");
        }

        return response;
    }

    /**
     * 공지사항 삭제
     * @param noticeId 삭제할 공지사항 ID
     * @return 삭제 성공 여부
     */
    @DeleteMapping("/notices/{noticeId}")
    public Map<String, Object> deleteNotice(@PathVariable int noticeId) {
        Map<String, Object> response = new HashMap<>();

        int result = nService.deleteNotice(noticeId);

        if (result > 0) {
            response.put("success", true);
            response.put("message", "공지사항이 삭제되었습니다.");
        } else {
            response.put("success", false);
            response.put("message", "공지사항 삭제에 실패했습니다.");
        }

        return response;
    }

    /**
     * 전체 병원 목록 조회
     * @return 병원 목록
     */
    @GetMapping("/hospitals")
    public List<Hospital> getAllHospitals() {
        return hService.getAllHospitals();
    }

    /**
     * 병원 추가
     * @param hospitalDTO 병원 정보 (진료과 포함)
     * @return 추가 성공 여부
     */
    @PostMapping("/hospitals")
    public Map<String, Object> createHospital(@RequestBody HospitalDTO hospitalDTO) {
        Map<String, Object> response = new HashMap<>();

        try {
            System.out.println("=== 병원 추가 시작 ===");
            System.out.println("Hospital ID: " + hospitalDTO.getHospitalId());
            System.out.println("Hospital Name: " + hospitalDTO.getHospitalName());
            System.out.println("Departments: " + hospitalDTO.getDepartments());

            // 병원 정보 저장
            hService.insertHospital(hospitalDTO);
            System.out.println("병원 정보 저장 완료");

            // 진료과 정보 저장
            if (hospitalDTO.getDepartments() != null && !hospitalDTO.getDepartments().trim().isEmpty()) {
                String[] departments = hospitalDTO.getDepartments().split(",");
                System.out.println("진료과 개수: " + departments.length);
                for (String dept : departments) {
                    String trimmedDept = dept.trim();
                    if (!trimmedDept.isEmpty()) {
                        System.out.println("진료과 저장 중: " + trimmedDept);
                        hService.insertHospitalDepartment(hospitalDTO.getHospitalId(), trimmedDept);
                    }
                }
                System.out.println("진료과 저장 완료");
            } else {
                System.out.println("진료과 정보 없음");
            }

            response.put("success", true);
            response.put("message", "병원이 추가되었습니다.");
        } catch (Exception e) {
            System.out.println("병원 추가 실패: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "병원 추가에 실패했습니다.");
        }

        return response;
    }

    /**
     * 병원 수정
     * @param hospitalId 병원 ID
     * @param hospitalDTO 수정할 병원 정보 (진료과 포함)
     * @return 수정 성공 여부
     */
    @PutMapping("/hospitals/{hospitalId}")
    public Map<String, Object> updateHospital(@PathVariable String hospitalId, @RequestBody HospitalDTO hospitalDTO) {
        Map<String, Object> response = new HashMap<>();

        try {
            hospitalDTO.setHospitalId(hospitalId);

            // 병원 정보 수정
            hService.updateHospital(hospitalDTO);

            // 기존 진료과 삭제
            hService.deleteHospitalDepartments(hospitalId);

            // 새로운 진료과 정보 저장
            if (hospitalDTO.getDepartments() != null && !hospitalDTO.getDepartments().trim().isEmpty()) {
                String[] departments = hospitalDTO.getDepartments().split(",");
                for (String dept : departments) {
                    String trimmedDept = dept.trim();
                    if (!trimmedDept.isEmpty()) {
                        hService.insertHospitalDepartment(hospitalId, trimmedDept);
                    }
                }
            }

            response.put("success", true);
            response.put("message", "병원 정보가 수정되었습니다.");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "병원 수정에 실패했습니다.");
        }

        return response;
    }

    /**
     * 병원 삭제
     * @param hospitalId 삭제할 병원 ID
     * @return 삭제 성공 여부
     */
    @DeleteMapping("/hospitals/{hospitalId}")
    public Map<String, Object> deleteHospital(@PathVariable String hospitalId) {
        Map<String, Object> response = new HashMap<>();

        try {
            // 먼저 해당 병원의 진료과 삭제
            hService.deleteHospitalDepartments(hospitalId);

            // 병원 삭제
            hService.deleteHospital(hospitalId);
            response.put("success", true);
            response.put("message", "병원이 삭제되었습니다.");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "병원 삭제에 실패했습니다.");
        }

        return response;
    }

    /**
     * 병원 진료과 목록 조회
     * @param hospitalId 병원 ID
     * @return 진료과 목록
     */
    @GetMapping("/hospitals/{hospitalId}/departments")
    public List<String> getHospitalDepartments(@PathVariable String hospitalId) {
        System.out.println("=== 진료과 목록 조회 ===");
        System.out.println("Hospital ID: " + hospitalId);
        List<String> departments = hService.getDepartmentsByHospitalId(hospitalId);
        System.out.println("진료과 개수: " + (departments != null ? departments.size() : 0));
        System.out.println("진료과 목록: " + departments);
        return departments;
    }

    /**
     * 일별 로그인 횟수 조회 (최근 7일)
     * @return 일별 로그인 횟수
     */
    @GetMapping("/stats/daily-login")
    public List<Map<String, Object>> getDailyLoginCount() {
        return alService.getDailyLoginCount();
    }

    /**
     * 전체 질환 목록 조회
     * @return 질환 목록
     */
    @GetMapping("/diseases")
    public List<Map<String, Object>> getAllDiseases() {
        return dService.findAllDiseases();
    }

    /**
     * 질환 추가 (이미지 포함)
     * @param diseaseName 질환명
     * @param description 설명
     * @param image 이미지 파일
     * @param departments 진료과 (쉼표로 구분)
     * @param symptoms 증상 (쉼표로 구분)
     * @return 추가 성공 여부
     */
    @PostMapping("/diseases")
    public Map<String, Object> createDisease(
            @RequestParam("diseaseName") String diseaseName,
            @RequestParam("description") String description,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam(value = "departments", required = false) String departments,
            @RequestParam(value = "symptoms", required = false) String symptoms) {
        Map<String, Object> response = new HashMap<>();

        try {
            Diseases disease = new Diseases();
            disease.setDiseaseName(diseaseName);
            disease.setDescription(description);

            // 이미지가 있으면 Cloudflare R2에 업로드
            if (image != null && !image.isEmpty()) {
                String sanitizedDiseaseName = diseaseName.replaceAll("[^a-zA-Z0-9가-힣_-]", "_");
                String extension = "";
                String originalFilename = image.getOriginalFilename();
                if (originalFilename != null && originalFilename.contains(".")) {
                    extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                }
                String fileName = sanitizedDiseaseName + extension;
                String imageUrl = r2Service.uploadFile(image, "__healbot__/disease/img", fileName);
                disease.setImageUrl(imageUrl);
            }

            int result = dService.addDisease(disease);

            if (result > 0) {
                // 진료과 추가
                if (departments != null && !departments.trim().isEmpty()) {
                    String[] deptArray = departments.split(",");
                    for (String dept : deptArray) {
                        String trimmedDept = dept.trim();
                        if (!trimmedDept.isEmpty()) {
                            dService.insertDiseaseDepartment(diseaseName, trimmedDept);
                        }
                    }
                }

                // 증상 추가
                if (symptoms != null && !symptoms.trim().isEmpty()) {
                    String[] symptomArray = symptoms.split(",");
                    for (String symptom : symptomArray) {
                        String trimmedSymptom = symptom.trim();
                        if (!trimmedSymptom.isEmpty()) {
                            dService.insertDiseaseSymptom(diseaseName, trimmedSymptom);
                        }
                    }
                }

                response.put("success", true);
                response.put("message", "질환이 추가되었습니다.");
            } else {
                response.put("success", false);
                response.put("message", "질환 추가에 실패했습니다.");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "질환 추가 중 오류가 발생했습니다: " + e.getMessage());
        }

        return response;
    }

    /**
     * 질환 수정 (이미지 포함)
     * @param diseaseNo 질환 번호
     * @param diseaseName 질환명
     * @param description 설명
     * @param image 이미지 파일 (선택)
     * @param departments 진료과 (쉼표로 구분)
     * @param symptoms 증상 (쉼표로 구분)
     * @return 수정 성공 여부
     */
    @PutMapping("/diseases/{diseaseNo}")
    public Map<String, Object> updateDisease(
            @PathVariable int diseaseNo,
            @RequestParam("diseaseName") String diseaseName,
            @RequestParam("description") String description,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam(value = "departments", required = false) String departments,
            @RequestParam(value = "symptoms", required = false) String symptoms) {
        Map<String, Object> response = new HashMap<>();

        try {
            Diseases disease = new Diseases();
            disease.setDiseaseNo(diseaseNo);
            disease.setDiseaseName(diseaseName);
            disease.setDescription(description);

            // 기존 질환 정보 조회
            Map<String, Object> existingDisease = dService.findByName(diseaseName);

            // 이미지가 있으면 기존 이미지 삭제 후 새 이미지 업로드
            if (image != null && !image.isEmpty()) {
                // 기존 이미지 삭제
                if (existingDisease != null && existingDisease.get("이미지") != null) {
                    String oldImageUrl = (String) existingDisease.get("이미지");
                    r2Service.deleteFile(oldImageUrl);
                }

                // 새 이미지 업로드
                String sanitizedDiseaseName = diseaseName.replaceAll("[^a-zA-Z0-9가-힣_-]", "_");
                String extension = "";
                String originalFilename = image.getOriginalFilename();
                if (originalFilename != null && originalFilename.contains(".")) {
                    extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                }
                String fileName = sanitizedDiseaseName + extension;
                String imageUrl = r2Service.uploadFile(image, "__healbot__/disease/img", fileName);
                disease.setImageUrl(imageUrl);
            }

            int result = dService.updateDisease(disease);

            if (result > 0) {
                // 기존 진료과 및 증상 삭제
                dService.deleteDiseaseDepartments(diseaseName);
                dService.deleteDiseaseSymptoms(diseaseName);

                // 진료과 추가
                if (departments != null && !departments.trim().isEmpty()) {
                    String[] deptArray = departments.split(",");
                    for (String dept : deptArray) {
                        String trimmedDept = dept.trim();
                        if (!trimmedDept.isEmpty()) {
                            dService.insertDiseaseDepartment(diseaseName, trimmedDept);
                        }
                    }
                }

                // 증상 추가
                if (symptoms != null && !symptoms.trim().isEmpty()) {
                    String[] symptomArray = symptoms.split(",");
                    for (String symptom : symptomArray) {
                        String trimmedSymptom = symptom.trim();
                        if (!trimmedSymptom.isEmpty()) {
                            dService.insertDiseaseSymptom(diseaseName, trimmedSymptom);
                        }
                    }
                }

                response.put("success", true);
                response.put("message", "질환이 수정되었습니다.");
            } else {
                response.put("success", false);
                response.put("message", "질환 수정에 실패했습니다.");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "질환 수정 중 오류가 발생했습니다: " + e.getMessage());
        }

        return response;
    }

    /**
     * 질환 삭제
     * @param diseaseNo 삭제할 질환 번호
     * @return 삭제 성공 여부
     */
    @DeleteMapping("/diseases/{diseaseNo}")
    public Map<String, Object> deleteDisease(@PathVariable int diseaseNo) {
        Map<String, Object> response = new HashMap<>();

        try {
            // 이미지 URL 조회 후 삭제
            Diseases disease = dService.findByDiseaseNo(diseaseNo);
            if (disease != null && disease.getImageUrl() != null) {
                r2Service.deleteFile(disease.getImageUrl());
            }

            int result = dService.deleteDisease(diseaseNo);

            if (result > 0) {
                response.put("success", true);
                response.put("message", "질환이 삭제되었습니다.");
            } else {
                response.put("success", false);
                response.put("message", "질환 삭제에 실패했습니다.");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "질환 삭제 중 오류가 발생했습니다: " + e.getMessage());
        }

        return response;
    }

}
