package com.example.backend.domain.response.converter;

import com.example.backend.domain.response.dto.ResponseQueryDto;
import com.example.backend.domain.response.entity.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ResponseQueryConverter {

    public static ResponseQueryDto toResponseQueryDto(ResponseEntity responseEntity) {
        return ResponseQueryDto.builder()
                .eventId(responseEntity.getEventId())
                .responses(responseEntity.getAnswers())
                .build();
    }

}
