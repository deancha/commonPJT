package com.web.blog.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationRequest {

    private String username;

    private String useremail;

    private String password;
    private String phonenumber;
    private String profileimg;
    private String introduction;
    private int age;
    private String gender;
    private String isadmin;
}
