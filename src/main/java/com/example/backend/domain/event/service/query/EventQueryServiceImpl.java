package com.example.backend.domain.event.service.query;

import com.example.backend.domain.event.converter.EventResponseConverter;
import com.example.backend.domain.event.dto.EventDetail;
import com.example.backend.domain.event.dto.EventResponseDto;
import com.example.backend.domain.event.dto.EventWithMetadataDto;
import com.example.backend.domain.event.entity.EventEntity;
import com.example.backend.domain.event.entity.EventType;
import com.example.backend.domain.event.repository.query.EventQueryRepository;
import com.example.backend.domain.user.entity.UserEntity;
import com.example.backend.global.error.BusinessException;
import com.example.backend.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.backend.domain.event.dto.EventResponseDto.*;

/**
 * 이벤트 Query Service 구현 클래스
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EventQueryServiceImpl implements EventQueryService{

    private final EventQueryRepository eventQueryRepository;
    private final EventResponseConverter eventResponseConverter;

    /**
     * 사용자의 진행중인 이벤트 리스트 조회
     * @param userEntity : 사용자 정보
     * @return SearchList
     */
    @Override
    @Transactional(readOnly = true)
    public SearchList getEventList(UserEntity userEntity) {

        List<EventWithMetadataDto> eventData = eventQueryRepository.findByUserIdAndIsActive(userEntity, true);

        return eventResponseConverter.toSearchListDto(eventData);
    }


    /**
     * 이벤트 상세 정보 조회
     * @param userEntity : 사용자 정보
     * @param eventId : 이벤트 ID
     * @return EventDetail
     */
    @Override
    @Transactional(readOnly = true)
    public EventDetail getEventDetail(UserEntity userEntity, Long eventId) {

        EventWithMetadataDto eventData = eventQueryRepository.findByUserIdAndEventIdAndIsActive(userEntity, eventId, true);

        if(eventData.event().getEventType() == EventType.FORM){
            return eventResponseConverter.toSearchFormDto(eventData.event(), eventData.metadata());
        } else if (eventData.event().getEventType() == EventType.FILE){
            return eventResponseConverter.toSearchFileDto(eventData.event(), eventData.metadata());
        } else {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
        }
    }

    /**
     * 이벤트 활성화 여부 조회
     * @param eventId : 이벤트 ID
     * @return Boolean
     */
    @Override
    @Transactional(readOnly = true)
    public IsActive getEventIsActive(Long eventId) {

        Boolean result = eventQueryRepository.existsById(eventId);

        return eventResponseConverter.toIsActiveDto(eventId, result);
    }

    /**
     * 이벤트 검색 컬럼 조회
     * @param eventId : 이벤트 ID
     * @return SearchColumnsAndEventId
     */
    @Override
    @Transactional(readOnly = true)
    public SearchColumnsAndEventId getEventColumns(Long eventId) {

        EventWithMetadataDto eventWithMetadata = eventQueryRepository.findByEventIdAndIsActive(eventId, true);

        // 이벤트가 존재하지 않으면 예외 발생
        if(eventWithMetadata == null) {
            throw new BusinessException(ErrorCode.EVENT_NOT_FOUND);
        }

        return eventResponseConverter.toSearchColumnsAndEventIdDto(eventWithMetadata);
    }

    /**
     * 다음 검색 이벤트들 조회
     * @param threshold
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<EventWithMetadataDto> getNextPollingEvents(LocalDateTime threshold) {
        return eventQueryRepository.findByNextPollingAtBefore(threshold);
    }
}
