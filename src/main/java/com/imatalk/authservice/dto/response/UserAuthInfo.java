package com.imatalk.authservice.dto.response;


import lombok.Data;

@Data
public class UserAuthInfo {
    private String id;
    private String email;
    private String username;
    private String hashPassword;

}
