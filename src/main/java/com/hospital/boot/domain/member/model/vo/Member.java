package com.hospital.boot.domain.member.model.vo;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.NoArgsConstructor;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Member {
	private String memberId;
	private String loginType;
	private String socialId;
	private String password;
	private String userName;
	private String email;
	private String phone;
	private String bornDate;
	private String gender;
	private String address;
	private Date createdAt;
	private String adminYn;
}
