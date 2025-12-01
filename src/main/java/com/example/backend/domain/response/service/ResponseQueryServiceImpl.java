package com.example.backend.domain.response.service;

import com.example.backend.domain.response.dto.ResponseQueryDto;
import com.example.backend.domain.response.entity.ResponseEntity;
import com.example.backend.global.error.BusinessException;
import com.example.backend.global.error.ErrorCode;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ResponseQueryServiceImpl implements ResponseQueryService{

    private final DynamoDbEnhancedClient dynamoDbEnhancedClient;
    private final DynamoDbTable<ResponseEntity> responseTable;

    public ResponseQueryServiceImpl(DynamoDbEnhancedClient dynamoDbEnhancedClient, DynamoDbTable<ResponseEntity> responseTable) {
        this.dynamoDbEnhancedClient = dynamoDbEnhancedClient;
        this.responseTable = responseTable;
    }

    /**
     * 응답을 조회하는 메서드
     * @param responseQueryDto : 조회할 응답 정보
     * @return 조회된 응답 정보
     */
    @Override
    public ResponseQueryDto getResponse(ResponseQueryDto responseQueryDto) {

        Long eventId = responseQueryDto.getEventId();
        Map<String, String> answers = responseQueryDto.getAnswers();
        PageIterable<ResponseEntity> result =
                responseTable.query(QueryConditional.keyEqualTo(k -> k.partitionValue(eventId)));

        List<ResponseEntity> items = new ArrayList<>();
        result.forEach(page -> items.addAll(page.items()));

        ResponseEntity matched = items.stream()
                .filter(entity -> {
                    Map<String, String> entityMap = entity.getAnswers();
                    if (entityMap == null) return false;

                    for (Map.Entry<String, String> entry : answers.entrySet()) {
                        String key = entry.getKey();
                        String expected = entry.getValue();

                        if (!expected.equals(entityMap.get(key))) {
                            return false;
                        }
                    }
                    return true;
                })
                .findFirst()
                .orElse(null);

        if (matched == null) {
            throw new BusinessException(ErrorCode.DATA_NOT_FOUND);
        }

        return ResponseQueryDto.of(matched);
    }
}
