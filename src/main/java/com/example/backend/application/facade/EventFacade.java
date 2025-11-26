package com.example.backend.application.facade;

import com.example.backend.domain.event.service.query.EventQueryService;
import com.example.backend.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventFacade {

    private final EventQueryService eventQueryService;
    private final UserService userService;


}
