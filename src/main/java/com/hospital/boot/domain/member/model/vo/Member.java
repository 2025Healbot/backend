package com.hospital.boot.domain.member.model.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.NoArgsConstructor;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Member {
    String memberId;
    String loginType;
    String socialId;
    String password;
    String userName;
    String email;
    String phone;
    String bornDate;
    String gender;
    String address;
    String createdDate;
    String adminYn;
}
