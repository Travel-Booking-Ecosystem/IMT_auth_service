package com.imatalk.authservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imatalk.authservice.dto.request.SaveUserRequest;
import com.imatalk.authservice.dto.response.CommonResponse;
import com.imatalk.authservice.dto.response.UserAuthInfo;
import com.imatalk.authservice.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class UserService {

    private final   WebClient.Builder webClientBuilder;


    public CommonResponse saveUser(SaveUserRequest saveUserRequest) {
        // send to user-service

        ResponseEntity<CommonResponse> responseEntity = webClientBuilder.build()
                .post()
                .uri("http://user-service/api/user/save")
                .bodyValue(saveUserRequest)
                .retrieve()
                .toEntity(CommonResponse.class)
                .block();

        if (responseEntity == null) {
            throw new RuntimeException("Something went wrong");
        }

        return responseEntity.getBody();
    }


    public UserAuthInfo getUserAuthInfoByEmail(String email) {
        ResponseEntity<CommonResponse> responseEntity = webClientBuilder.build()
                .get()
                .uri("http://user-service/api/user/auth-info-by-email/" + email)
                .retrieve()
                .toEntity(CommonResponse.class)
                .block();

        if (responseEntity == null) {
            throw new RuntimeException("Something went wrong");
        }


        CommonResponse body = responseEntity.getBody();

        if (body == null) {
            return null;
        }
        return new ObjectMapper().convertValue(body.getData(), UserAuthInfo.class);

    }
}
