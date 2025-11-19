package com.hospital.boot.app.admin.controller;

import com.hospital.boot.domain.admin.model.service.AdminService;
import com.hospital.boot.domain.member.model.vo.Member;
import com.hospital.boot.domain.notice.model.service.NoticeService;
import com.hospital.boot.domain.notice.model.vo.Notice;
import com.hospital.boot.domain.hospital.model.service.HospitalService;
import com.hospital.boot.domain.hospital.model.vo.Hospital;
import org.springframework.web.bind.annotation.*;
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

        notice.setNoticeId(noticeId);
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
     * @param hospital 병원 정보
     * @return 추가 성공 여부
     */
    @PostMapping("/hospitals")
    public Map<String, Object> createHospital(@RequestBody Hospital hospital) {
        Map<String, Object> response = new HashMap<>();

        try {
            hService.insertHospital(hospital);
            response.put("success", true);
            response.put("message", "병원이 추가되었습니다.");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "병원 추가에 실패했습니다.");
        }

        return response;
    }

    /**
     * 병원 수정
     * @param hospitalId 병원 ID
     * @param hospital 수정할 병원 정보
     * @return 수정 성공 여부
     */
    @PutMapping("/hospitals/{hospitalId}")
    public Map<String, Object> updateHospital(@PathVariable String hospitalId, @RequestBody Hospital hospital) {
        Map<String, Object> response = new HashMap<>();

        try {
            hospital.setHospitalId(hospitalId);
            hService.updateHospital(hospital);
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

}
