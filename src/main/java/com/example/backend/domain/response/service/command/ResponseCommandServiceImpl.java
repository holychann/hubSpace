package com.example.backend.domain.response.service.command;

import com.example.backend.domain.event.entity.EventEntity;
import com.example.backend.domain.response.entity.ResponseEntity;
import com.example.backend.infra.google.dto.GoogleFormResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.WriteBatch;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.BatchWriteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.WriteRequest;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ResponseCommandServiceImpl implements ResponseCommandService{

    private final DynamoDbTable<ResponseEntity> responseTable;
    private final DynamoDbEnhancedClient dynamoDbEnhancedClient;

    @Override
    public void saveResponses(List<GoogleFormResponseDto> responses, Long eventId) {

        int batchSize = 25;

        for(int i = 0; i < responses.size(); i += batchSize){
            int end = Math.min(i + batchSize, responses.size());
            List<GoogleFormResponseDto> batch = responses.subList(i, end);

            List<ResponseEntity> responseEntities = ResponseEntity.fromGoogleFormList(batch, eventId);


            WriteBatch.Builder<ResponseEntity> writeBatchBuilder =
                    WriteBatch.builder(ResponseEntity.class)
                            .mappedTableResource(responseTable);

            responseEntities.forEach(writeBatchBuilder::addPutItem);

            WriteBatch writeBatch = writeBatchBuilder.build();

            dynamoDbEnhancedClient.batchWriteItem(b -> b.writeBatches(writeBatch));
        }

    }
}
