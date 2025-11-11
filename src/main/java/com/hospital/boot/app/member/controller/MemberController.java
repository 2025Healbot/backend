package com.hospital.boot.app.member.controller;

import com.hospital.boot.domain.member.model.service.MemberService;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService mService;

    @PostMapping("")
    public int checkTest(@RequestBody Map<String, String> request){
        try {
            String id = request.get("id");
            String pw = request.get("pw");
            if(id.equals("123") && pw.equals("123")){
              return 1;
            }
        }catch (Exception e){
            return -1;
        }

        return 0;
    }

}
