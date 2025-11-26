package com.example.backend.application.facade;

import com.example.backend.domain.event.dto.EventDetail;
import com.example.backend.domain.event.dto.EventRequestDto;
import com.example.backend.domain.event.dto.EventResponseDto;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface EventFacade {

    EventResponseDto.SearchList getEventList(OAuth2User user);
    EventDetail getEventDetail(OAuth2User user, Long eventId);
    EventResponseDto.CreatedFormEvent createFormEvent(OAuth2User user, EventRequestDto.CreateFormEvent eventRequestDto);

}
