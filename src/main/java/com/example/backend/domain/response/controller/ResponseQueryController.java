package com.example.backend.domain.response.controller;

import com.example.backend.application.facade.ResponseFacade;
import com.example.backend.domain.response.dto.ResponseQueryDto;
import com.example.backend.global.dto.ApiResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ResponseQueryController {

    private final ResponseFacade responseFacade;

    /**
     * 답변 조회
     * @param params : 답변 조회 조건
     * @return ApiResponseDto
     */
    @GetMapping("/events/{eventId}/search")
    public ResponseEntity<ApiResponseDto> getResponse(
            @PathVariable Long eventId,
            @RequestParam Map<String, String> params
            ){

        // DTO로 변환
        ResponseQueryDto queryDto = ResponseQueryDto.of(eventId, params);

        long start = System.currentTimeMillis();

        log.info(" >>> [RESPONSE][CTRL][REQUEST] /response GET start | eventId: {}, params: {}", eventId, params);

        ResponseQueryDto response = responseFacade.getResponse(queryDto);

        long end = System.currentTimeMillis();
        long elapsed = end - start;

        log.info(" >>> [RESPONSE][CTRL][RESPONSE] /response GET end | 걸린시간: {}", elapsed);

        return ResponseEntity.ok(ApiResponseDto.success(response));

    }

}
