package com.example.backend.domain.response.entity;


import com.example.backend.infra.google.dto.GoogleFormResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
    public ResponseEntity fromGoogleForm(GoogleFormResponseDto response, Long eventId) {
        return ResponseEntity.builder()
                .eventId(eventId)
                .answers(response.getAnswers())
                .createTime(response.getCreateTime())
                .ttl(Instant.now().plus(20, ChronoUnit.DAYS).getEpochSecond())
                .build();
    }
}
