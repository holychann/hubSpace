package com.example.backend.domain.response.entity;


import lombok.Builder;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.util.Map;

/**
 * DynamoDB Enhanced Client 에서 Lombok 이 인식되지 않음으로 Getter 와 Setter 를 수동으로 추가함.
 */
@Builder
@DynamoDbBean
public class ResponseEntity {
    private Long eventId;
    private String createTime;
    private Map<String, String> answers;
    private Long ttl;

    @DynamoDbPartitionKey
    @DynamoDbAttribute("user_id")
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
}
