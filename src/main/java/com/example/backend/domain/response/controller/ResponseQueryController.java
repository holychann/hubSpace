package com.example.backend.domain.response.controller;

import com.example.backend.application.facade.ResponseFacade;
import com.example.backend.domain.response.dto.ResponseQueryDto;
import com.example.backend.global.dto.ApiResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/response")
public class ResponseQueryController {

    private final ResponseFacade responseFacade;

    /**
     * 답변 조회
     * @param params : 답변 조회 조건
     * @return ApiResponseDto
     */
    @GetMapping()
    public ResponseEntity<ApiResponseDto> getResponse(
            @RequestParam Map<String, String> params
            ){

        /**
         * 1️⃣ 이벤트 ID 파라미터 추출
         * 2️⃣ 이벤트 ID 제거
         * 3️⃣ 답변 조회 조건 DTO 생성
         */
        Long eventId = Long.parseLong(params.get("eventId"));
        params.remove("eventId");
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
