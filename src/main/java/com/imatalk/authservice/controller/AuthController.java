package com.imatalk.authservice.controller;

import com.imatalk.authservice.dto.request.LoginRequest;
import com.imatalk.authservice.dto.request.RegistrationRequest;
import com.imatalk.authservice.dto.response.CommonResponse;
import com.imatalk.authservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RequestMapping("/api/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/health")
    public ResponseEntity<CommonResponse> health() {
        Map<String, String> map = Map.of(
                "service", "auth-service",
                "status", "OK",
                "time", LocalDateTime.now().toString());
        return ResponseEntity.ok(CommonResponse.success("Health check",map));
    }

    @PostMapping("/register")
    public ResponseEntity<CommonResponse> register(@RequestBody @Validated RegistrationRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public ResponseEntity<CommonResponse> register(@RequestBody  @Validated LoginRequest request) {
        return authService.login(request);
    }

}
