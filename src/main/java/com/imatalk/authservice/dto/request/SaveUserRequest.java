package com.imatalk.authservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaveUserRequest {
    private String hashPassword;
    private String email;
    private String displayName;
    private String username;
}
