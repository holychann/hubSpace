package com.example.backend.infra.polling;

import com.example.backend.domain.event.dto.EventWithMetadataDto;
import com.example.backend.domain.event.service.command.EventCommandService;
import com.example.backend.domain.event.service.query.EventQueryService;
import com.example.backend.domain.response.service.command.ResponseCommandService;
import com.example.backend.infra.google.drive.GoogleDriveService;
import com.example.backend.infra.google.dto.GoogleFormResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Component
public class Poller {

    private final EventQueryService eventQueryService;
    private final GoogleDriveService googleDriveService;
    private final ResponseCommandService responseCommandService;
    private final EventCommandService eventCommandService;

    @Transactional
    @Scheduled(fixedRate = 60_000) // 매 1분
    public void scanAndPoll() throws IOException {

        log.info("🤖[POLLING] 검색 시작");

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime threshold = now.plusMinutes(2);

        List<EventWithMetadataDto> targets = eventQueryService.getNextPollingEvents(threshold);

        for (EventWithMetadataDto event : targets) {

            String validAccessToken = googleDriveService.getValidAccessToken(event.event().getUser().getUsername());

            // drive에서 form 응답 가져오기
            List<GoogleFormResponseDto> formResponses = googleDriveService.getFormResponses(
                    event.metadata().getFormId(), validAccessToken, event.metadata().getSearchColumnsIds(),
                    event.event().getLastResponseTime()
            );

            long days = ChronoUnit.DAYS.between(event.event().getCreatedAt(), LocalDateTime.now());
            Duration interval;
            if (days <= 7) interval = Duration.ofMinutes(1);
            else if (days <= 10) interval = Duration.ofHours(1);
            else if (days < 20) interval = Duration.ofHours(6);
            else {
                eventCommandService.updateEventStatus(event.event(), false);
                continue;
            }

            if(formResponses.isEmpty()){
                eventCommandService.updateNextPollingAt(event.event(), LocalDateTime.now().plus(interval));
                continue;
            }

            // 응답 저장
            responseCommandService.saveResponses(formResponses, event.event().getId());

            eventCommandService.updateCount(event.event(), (long) formResponses.size());

            // 다음 검색 시간 업데이트
            String createTimeStr = formResponses.get(formResponses.size() - 1).getCreateTime();
            LocalDateTime laseCreateTime = OffsetDateTime.parse(createTimeStr).toLocalDateTime();
            eventCommandService.updateNextPollingAtAndLastResponseTime(event.event(), LocalDateTime.now().plus(interval), laseCreateTime);
        }
    }

}
