package com.hospital.boot.app.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SocialUser {
    private String id;
    private String email;
    private String name;
    private String profileImage;
}
