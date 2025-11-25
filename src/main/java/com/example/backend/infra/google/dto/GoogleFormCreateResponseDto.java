package com.example.backend.infra.google.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GoogleFormCreateResponseDto {
    private String formId;
    private String formUrl;

    public static GoogleFormCreateResponseDto of(String formId, String formUrl) {
        return GoogleFormCreateResponseDto.builder()
                .formId(formId)
                .formUrl(formUrl)
                .build();
    }
}
