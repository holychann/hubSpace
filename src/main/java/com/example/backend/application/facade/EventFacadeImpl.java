package com.example.backend.application.facade;

import com.example.backend.domain.event.dto.EventDetail;
import com.example.backend.domain.event.dto.EventRequestDto;
import com.example.backend.domain.event.dto.EventResponseDto;
import com.example.backend.domain.event.service.command.EventCommandService;
import com.example.backend.domain.event.service.query.EventQueryService;
import com.example.backend.domain.user.entity.UserEntity;
import com.example.backend.domain.user.service.UserService;
import com.example.backend.global.dto.ApiResponseDto;
import com.example.backend.infra.google.drive.GoogleDriveService;
import com.example.backend.infra.google.dto.GoogleFormCreateResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import static com.example.backend.domain.event.dto.EventResponseDto.*;

/**
 * 이벤트 Facade 구현 클래스
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class EventFacadeImpl implements EventFacade{

    private final EventQueryService eventQueryService;
    private final UserService userService;
    private final GoogleDriveService googleDriveService;
    private final EventCommandService eventCommandService;


    /**
     * 이벤트 리스트 조회
     * @param user : 로그인한 사용자 정보
     * @return SearchList
     */
    @Override
    public SearchList getEventList(OAuth2User user) {

        String email = (String) user.getAttributes().get("email");
        UserEntity userEntity = userService.findUserBySocialEmail(email);

        return eventQueryService.getEventList(userEntity.getId());
    }

    /**
     * 이벤트 상세 정보 조회
     * @param user : 로그인한 사용자 정보
     * @param eventId : 이벤트 ID
     * @return EventDetail
     */
    @Override
    public EventDetail getEventDetail(OAuth2User user, Long eventId) {

        String email = (String) user.getAttributes().get("email");
        UserEntity userEntity = userService.findUserBySocialEmail(email);

        return eventQueryService.getEventDetail(userEntity.getId(), eventId);
    }

    /**
     * Google Form 이벤트 생성
     * @param user : 로그인한 사용자 정보
     * @param eventRequestDto : Google Form 생성 요청 DTO
     * @return CreatedFormEvent
     */
    @Override
    public CreatedFormEvent createFormEvent(OAuth2User user, EventRequestDto.CreateFormEvent eventRequestDto) {

        String email = (String) user.getAttributes().get("email");
        UserEntity userEntity = userService.findUserBySocialEmail(email);

        GoogleFormCreateResponseDto googleFormResponse = googleDriveService.createFormInDrive(
                userEntity.getUsername(),
                eventRequestDto.getEventTitle(),
                eventRequestDto.getSearchColumns(),
                userEntity.getGoogleRefreshToken());

        return eventCommandService.createFormEvent(userEntity, eventRequestDto, googleFormResponse);
    }
}
