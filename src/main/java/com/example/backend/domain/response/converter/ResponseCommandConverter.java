package com.example.backend.domain.response.converter;

import com.example.backend.domain.response.entity.ResponseEntity;
import com.example.backend.infra.google.dto.GoogleFormResponseDto;
import org.springframework.stereotype.Component;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
public class ResponseCommandConverter {

    public static ResponseEntity googleFormResponseToEntity(GoogleFormResponseDto response, Long eventId) {
        return ResponseEntity.builder()
                .eventId(eventId)
                .answers(response.getAnswers())
                .createTime(response.getCreateTime())
                .ttl(Instant.now().plus(20, ChronoUnit.DAYS).getEpochSecond())
                .build();
    }
}
