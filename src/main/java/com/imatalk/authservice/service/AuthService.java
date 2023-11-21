package com.imatalk.authservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imatalk.authservice.dto.request.LoginRequest;
import com.imatalk.authservice.dto.request.RegistrationRequest;
import com.imatalk.authservice.dto.request.SaveUserRequest;
import com.imatalk.authservice.dto.response.CommonResponse;
import com.imatalk.authservice.dto.response.LoginResponse;
import com.imatalk.authservice.dto.response.UserAuthInfo;
import com.imatalk.authservice.dto.response.UserProfile;
import com.imatalk.authservice.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserService userService;
    private final KafkaProducerService kafkaProducerService;

    public ResponseEntity<CommonResponse> register(RegistrationRequest request) {

        SaveUserRequest saveUserRequest = SaveUserRequest.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .displayName(request.getDisplayName())
                .hashPassword(passwordEncoder.encode(request.getPassword()))
                .build();
        CommonResponse commonResponse = userService.saveUser(saveUserRequest);

        if (commonResponse.getStatus() == 200) {
            // convert object to UserProfile
            UserProfile userProfile = new ObjectMapper().convertValue(commonResponse.getData(), UserProfile.class);
            kafkaProducerService.sendNewRegisterUserEvent(userProfile);
            return ResponseEntity.ok(CommonResponse.success("Registration success"));
        } else {
            return ResponseEntity.ok(CommonResponse.error(commonResponse.getMessage()));

        }
    }

    public ResponseEntity<CommonResponse> login(LoginRequest request) {

        UserAuthInfo userAuthInfo = userService.getUserAuthInfoByEmail(request.getEmail());

        if (userAuthInfo == null || !passwordEncoder.matches(request.getPassword(), userAuthInfo.getHashPassword())) {
            throw new ApplicationException("Incorrect credentials");
        }

        String token = jwtService.generateToken(userAuthInfo);
        LoginResponse response = LoginResponse.builder()
                .accessToken(token)
                .build();

        return ResponseEntity.ok(CommonResponse.success("Login success", response));
    }

}
