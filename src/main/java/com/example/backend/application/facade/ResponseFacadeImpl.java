package com.example.backend.application.facade;

import com.example.backend.domain.event.dto.EventResponseDto;
import com.example.backend.domain.event.service.query.EventQueryService;
import com.example.backend.domain.response.dto.ResponseQueryDto;
import com.example.backend.domain.response.service.query.ResponseQueryService;
import com.example.backend.global.error.BusinessException;
import com.example.backend.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.example.backend.application.facade.helper.ResponseHelper.extracted;

@Service
@Slf4j
@RequiredArgsConstructor
public class ResponseFacadeImpl implements ResponseFacade{

    private final EventQueryService eventQueryService;
    private final ResponseQueryService responseQueryService;

    /**
     * 답변 조회
     * @param queryDto : 조회할 답변 정보
     * @return ResponseQueryDto
     */
    @Override
    public ResponseQueryDto getResponse(ResponseQueryDto queryDto) {

        log.info("🗒️[RESPONSE][FACADE][GET] 답변 조회 요청 | queryDto: {}", queryDto);
        /**
         * 해당 메서드 역할 :
         * ✅ 이벤트 존재 여부 확인
         * ✅ 이벤트 활성화 여부 확인
         * ✅ 이벤트 검색 컬럼 조회
         * ❗ 확인이 안되면 BusinessException 발생
         */
        EventResponseDto.SearchColumnsAndEventId eventColumns = eventQueryService.getEventColumns(queryDto.getEventId());

        // 검색 컬럼과 답변 데이터 키가 일치하는지 확인
        if(!extracted(queryDto, eventColumns)){
            log.error("❌ [RESPONSE][FACADE][GET] 검색 컬럼과 답변 데이터 키가 일치하지 않습니다.");
            throw new BusinessException(ErrorCode.DATA_INVALID_INPUT_VALUE);
        }

        ResponseQueryDto response = responseQueryService.getResponse(queryDto);

        return response;
    }


}
