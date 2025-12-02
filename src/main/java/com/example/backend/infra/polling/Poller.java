package com.example.backend.infra.polling;

import com.example.backend.domain.event.dto.EventWithMetadataDto;
import com.example.backend.domain.event.entity.EventEntity;
import com.example.backend.domain.event.repository.command.EventCommandRepository;
import com.example.backend.domain.event.service.command.EventCommandService;
import com.example.backend.domain.event.service.query.EventQueryService;
import com.example.backend.domain.response.service.command.ResponseCommandService;
import com.example.backend.infra.google.drive.GoogleDriveService;
import com.example.backend.infra.google.dto.GoogleFormResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RequiredArgsConstructor
public class Poller {

    private final EventQueryService eventQueryService;
    private final GoogleDriveService googleDriveService;
    private final ResponseCommandService responseCommandService;
    private final EventCommandService eventCommandService;

    @Scheduled(fixedRate = 60_000) // 매 1분
    public void scanAndPoll() throws IOException {

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

            // 응답 저장
            responseCommandService.saveResponses(formResponses);

            long days = ChronoUnit.DAYS.between(event.event().getCreatedAt(), LocalDateTime.now());
            Duration interval;
            if (days <= 7) interval = Duration.ofMinutes(1);
            else if (days <= 10) interval = Duration.ofHours(1);
            else if (days <= 30) interval = Duration.ofHours(6);
            else {
                eventCommandService.updateEventStatus(event.event(), false);
                continue;
            }

            // 다음 검색 시간 업데이트
            eventCommandService.updateNextPollingAt(event.event(), LocalDateTime.now().plus(interval));
        }
    }

}
