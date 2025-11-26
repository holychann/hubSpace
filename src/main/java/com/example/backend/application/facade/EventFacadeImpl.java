package com.example.backend.application.facade;

import com.example.backend.domain.event.dto.EventDetail;
import com.example.backend.domain.event.dto.EventResponseDto;
import com.example.backend.domain.event.service.query.EventQueryService;
import com.example.backend.domain.user.entity.UserEntity;
import com.example.backend.domain.user.service.UserService;
import com.example.backend.global.dto.ApiResponseDto;
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


    /**
     * 이벤트 리스트 조회
     * @param user : 로그인한 사용자 정보
     * @return SearchList
     */
    @Override
    public SearchList getEventList(OAuth2User user) {

        String email = (String) user.getAttributes().get("email");
        UserEntity userEntity = userService.findUserBySocialEmail(email);

        SearchList eventList = eventQueryService.getEventList(userEntity.getId());

        return eventList;
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

        EventDetail eventDetail = eventQueryService.getEventDetail(userEntity.getId(), eventId);

        return eventDetail;
    }

}
