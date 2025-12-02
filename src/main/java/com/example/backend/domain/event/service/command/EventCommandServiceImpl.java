package com.example.backend.domain.event.service.command;

import com.example.backend.domain.event.converter.EventRequestConverter;
import com.example.backend.domain.event.converter.EventResponseConverter;
import com.example.backend.domain.event.dto.EventRequestDto;
import com.example.backend.domain.event.dto.EventResponseDto;
import com.example.backend.domain.event.entity.EventEntity;
import com.example.backend.domain.event.entity.EventMetadataEntity;
import com.example.backend.domain.event.repository.command.EventCommandRepository;
import com.example.backend.domain.event.repository.command.EventMetadataCommandRepository;
import com.example.backend.domain.event.repository.query.EventMetadataQueryRepository;
import com.example.backend.domain.user.entity.UserEntity;
import com.example.backend.infra.google.dto.GoogleFormCreateResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventCommandServiceImpl implements EventCommandService{

    private final EventCommandRepository eventCommandRepository;
    private final EventMetadataCommandRepository eventMetadataCommandRepository;
    private final EventRequestConverter eventRequestConverter;
    private final EventResponseConverter eventResponseConverter;
    private final EventMetadataQueryRepository eventMetadataQueryRepository;

    @Override
    public EventResponseDto.CreatedFormEvent createFormEvent(
            UserEntity userEntity, EventRequestDto.CreateFormEvent eventRequestDto,
            GoogleFormCreateResponseDto googleFormCreateResponseDto
    ) {

        log.info("[EVENT][SERVICE][CREATE] 구글 폼 이벤트 저장 시작 | formId: {}", googleFormCreateResponseDto.getFormId());


        EventEntity eventEntity = eventRequestConverter.formDtoToEntity(eventRequestDto, userEntity);

        EventMetadataEntity eventMetadataEntity = eventRequestConverter.formDtoToMetadataEntity(
                eventRequestDto,
                eventEntity,
                googleFormCreateResponseDto);

        EventEntity save = eventCommandRepository.save(eventEntity);
        eventMetadataCommandRepository.save(eventMetadataEntity);

        log.info("[EVENT][SERVICE][CREATE] 구글 폼 이벤트 저장 완료 | eventId: {}", save.getId());

        return eventResponseConverter.toCreatedFormEventDto(save, googleFormCreateResponseDto);
    }

    /**
     * 마지막 응답 시간 업데이트
     * @param eventEntity : 이벤트 정보
     * @param lastResponseTime : 마지막 응답 시간
     */
    @Override
    public void updateLastResponseTime(EventEntity eventEntity, LocalDateTime lastResponseTime) {
        eventEntity.updateLastResponseTime(lastResponseTime);
        eventCommandRepository.save(eventEntity);
    }

    /**
     * 다음 검색 시간 업데이트
     * @param eventEntity : 이벤트 정보
     * @param nextPollingAt : 다음 검색 시간
     */
    @Override
    public void updateNextPollingAtAndLastResponseTime(EventEntity eventEntity, LocalDateTime nextPollingAt, LocalDateTime lastResponseTime) {
        eventEntity.updateNextPollingAt(nextPollingAt);
        eventEntity.updateLastResponseTime(lastResponseTime);
        eventCommandRepository.save(eventEntity);
    }

    /**
     * 이벤트 활성화 상태 업데이트
     * @param eventEntity : 이벤트 정보
     * @param isActive : 활성화 여부
     */
    @Override
    public void updateEventStatus(EventEntity eventEntity, Boolean isActive) {
        eventEntity.updateIsActive(isActive);
        eventCommandRepository.save(eventEntity);
    }

    /**
     * 다음 검색 시간 업데이트
     * @param eventEntity : 이벤트 정보
     * @param nextPollingAt : 다음 검색 시간
     */
    @Override
    public void updateNextPollingAt(EventEntity eventEntity, LocalDateTime nextPollingAt) {
        eventEntity.updateNextPollingAt(nextPollingAt);
        eventCommandRepository.save(eventEntity);
    }

    /**
     * 응답 수 업데이트
     * @param eventEntity : 이벤트 정보
     * @param count : 응답 수
     */
    @Override
    public void updateCount(EventEntity eventEntity, Long count) {
        EventMetadataEntity meta = eventMetadataQueryRepository.findByEventId(eventEntity.getId());
        meta.updateCount(meta.getCount() + count);
    }
}
