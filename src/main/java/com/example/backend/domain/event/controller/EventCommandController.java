package com.example.backend.domain.event.controller;

import com.example.backend.application.facade.EventFacade;
import com.example.backend.domain.event.dto.EventRequestDto;
import com.example.backend.domain.event.dto.EventResponseDto;
import com.example.backend.global.dto.ApiResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Slf4j
public class EventCommandController {

    private final EventFacade eventFacade;

    @PostMapping("/form")
    public ResponseEntity<ApiResponseDto> createFormEvent(
            @AuthenticationPrincipal String username,
            @RequestBody EventRequestDto.CreateFormEvent eventRequestDto
    ) {
        log.info("[EVENT][CTRL][REQUEST] /events/form POST start | username: {}", username);

        long start = System.currentTimeMillis();

        EventResponseDto.CreatedFormEvent formInfo = eventFacade.createFormEvent(username, eventRequestDto);

        long end = System.currentTimeMillis();
        long elapsed = end - start;

        log.info("[EVENT][CTRL][RESPONSE] /events/form POST end | 걸린시간: {}", elapsed);

        return ResponseEntity.ok(ApiResponseDto.success(formInfo));
    }

    // TODO: CSV 이벤트용 API 로직 구현

}
