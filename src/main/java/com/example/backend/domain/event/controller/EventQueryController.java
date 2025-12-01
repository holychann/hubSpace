package com.example.backend.domain.event.controller;

import com.example.backend.application.facade.EventFacade;
import com.example.backend.domain.event.dto.EventDetail;
import com.example.backend.domain.event.service.query.EventQueryService;
import com.example.backend.global.dto.ApiResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.backend.domain.event.dto.EventResponseDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
@Slf4j
public class EventQueryController {

    private final EventFacade eventFacade;
    private final EventQueryService eventQueryService;

    /**
     * 사용자의 진행중인 이벤트 리스트 조회
     * @param username : 로그인한 사용자 정보
     * @return SearchList
     */
    @GetMapping("")
    public ResponseEntity<ApiResponseDto<SearchList>> searchLish(
            @AuthenticationPrincipal String username
    ){
        long start = System.currentTimeMillis();
        log.info("[EVENT][CTRL][REQUEST] /events GET start | username: {}", username);

        SearchList eventList = eventFacade.getEventList(username);

        long end = System.currentTimeMillis();
        long elapsed = end - start;

        log.info("[EVENT][CTRL][RESPONSE] /events GET end | 걸린시간: {}", elapsed);

        return ResponseEntity.ok(ApiResponseDto.success(eventList));
    }

    /**
     * 이벤트 상세 정보 조회
     * @param id : 이벤트 ID
     * @param username : 로그인한 사용자 정보
     * @return EventDetail
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<EventDetail>> searchEvent(
            @PathVariable("id") Long id,
            @AuthenticationPrincipal String username
    ){
        long start = System.currentTimeMillis();
        log.info("[EVENT][CTRL][REQUEST] /events/{} GET start | username: {}", id, username);

        EventDetail eventDetail = eventFacade.getEventDetail(username, id);

        long end = System.currentTimeMillis();
        long elapsed = end - start;
        log.info("[EVENT][CTRL][RESPONSE] /events/{} GET end | 걸린시간: {}", id, elapsed);

        return ResponseEntity.ok(ApiResponseDto.success(eventDetail));
    }

    /**
     * 이벤트가 존재하는지 확인
     * @param id : 이벤트 ID
     * @return IsActive
     */
    @GetMapping("/{id}/isActive")
    public ResponseEntity<ApiResponseDto<IsActive>> isExistsEvent(
            @PathVariable("id") Long id
    ){
        long start = System.currentTimeMillis();

        log.info("[EVENT][CTRL][REQUEST] /events/{}/isActive GET start", id);

        IsActive isActive = eventQueryService.getEventIsActive(id);

        long end = System.currentTimeMillis();
        long elapsed = end - start;

        log.info("[EVENT][CTRL][RESPONSE] /events/{}/isActive GET end | 걸린시간: {}", id, elapsed);

        return ResponseEntity.ok(ApiResponseDto.success(isActive));

    }

}
