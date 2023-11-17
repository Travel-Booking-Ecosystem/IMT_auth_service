package com.imatalk.authservice.service;

import com.imatalk.authservice.dto.response.UserProfile;
import com.imatalk.authservice.event.NewRegisteredUserEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${topic.new-registered-user}")
    private String NEW_REGISTER_USER_TOPIC;


    public void sendNewRegisterUserEvent(UserProfile userProfile) {
        NewRegisteredUserEvent event = NewRegisteredUserEvent.builder()
                .userId(userProfile.getId())
                .username(userProfile.getUsername())
                .displayName(userProfile.getDisplayName())
                .avatar(userProfile.getAvatar())
                .build();
        kafkaTemplate.send(NEW_REGISTER_USER_TOPIC, event);
    }
}
