package com.example.backend.domain.response.entity;


import com.example.backend.infra.google.dto.GoogleFormResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DynamoDB Enhanced Client 에서 Lombok 이 인식되지 않음으로 Getter 와 Setter 를 수동으로 추가함.
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamoDbBean
public class ResponseEntity {
    private Long eventId;
    private String createTime;
    private Map<String, String> answers;
    private Long ttl;

    @DynamoDbPartitionKey
    @DynamoDbAttribute("eventId")
    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }
    @DynamoDbAttribute("createTime")
    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @DynamoDbAttribute("answers")
    public Map<String, String> getAnswers() {
        return answers;
    }

    public void setAnswers(Map<String, String> answers) {
        this.answers = answers;
    }

    @DynamoDbAttribute("ttl")
    public Long getTtl() {
        return ttl;
    }

    public void setTtl(Long ttl) {
        this.ttl = ttl;
    }

    /**
     * Google Form 응답을 DynamoDB Entity 로 변환하는 메서드
     * @param response : Google Form 응답 DTO
     * @param eventId : 이벤트 ID
     * @return ResponseEntity
     */
    public static ResponseEntity fromGoogleForm(GoogleFormResponseDto response, Long eventId) {
        return ResponseEntity.builder()
                .eventId(eventId)
                .answers(response.getAnswers())
                .createTime(response.getCreateTime())
                .ttl(Instant.now().plus(20, ChronoUnit.DAYS).getEpochSecond())
                .build();
    }

    public static List<ResponseEntity> fromGoogleFormList(List<GoogleFormResponseDto> responses, Long eventId) {
        return responses
                .stream()
                .map(response -> ResponseEntity.fromGoogleForm(response, eventId))
                .toList();
    }

    public Map<String, AttributeValue> toAttributeValueMap() {
        Map<String, AttributeValue> item = new HashMap<>();

        // 숫자 → n()
        item.put("eventId", AttributeValue.builder().n(String.valueOf(eventId)).build());
        // 문자열 → s()
        item.put("createTime", AttributeValue.builder().s(createTime).build());
        // Map<String, String> → m() + AttributeValue 변환
        Map<String, AttributeValue> answersAttr = new HashMap<>();
        answers.forEach((key, value) -> {
            answersAttr.put(key, AttributeValue.builder().s(value).build());
        });
        item.put("answers", AttributeValue.builder().m(answersAttr).build());
        // TTL 필드 → n()
        item.put("ttl", AttributeValue.builder().n(String.valueOf(ttl)).build());

        return item;
    }



}
