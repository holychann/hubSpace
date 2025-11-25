package com.example.backend.global.handler;


import com.example.backend.domain.jwt.service.JwtService;
import com.example.backend.global.event.GoogleRefreshTokenListener;
import com.example.backend.global.util.JWTUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Qualifier("SocialSuccessHandler")
@Slf4j
public class SocialSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final OAuth2AuthorizedClientService authorizedClientService;

    private ApplicationEventPublisher eventPublisher;

    public SocialSuccessHandler(JwtService jwtService, OAuth2AuthorizedClientService authorizedClientService, ApplicationEventPublisher eventPublisher) {
        this.jwtService = jwtService;
        this.authorizedClientService = authorizedClientService;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException, IOException {

        // username, role
        String username =  authentication.getName();
        String role = authentication.getAuthorities().iterator().next().getAuthority();

        // JWT(Refresh) 발급
        String refreshToken = JWTUtil.createJWT(username, "ROLE_" + role, false);

        // 발급한 Refresh DB 테이블 저장 (Refresh whitelist)
        jwtService.addRefresh(username, refreshToken);

        // 응답
        Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(false);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(10); // 10초 (프론트에서 발급 후 바로 헤더 전환 로직 진행 예정)

        response.addCookie(refreshCookie);
        response.sendRedirect("http://localhost:5173/cookie");

        OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;

        OAuth2AuthorizedClient client =
                authorizedClientService.loadAuthorizedClient(
                        authToken.getAuthorizedClientRegistrationId(),
                        authToken.getName()
                );

        String googleRefreshToken =
                client.getRefreshToken() != null ? client.getRefreshToken().getTokenValue() : null;
        log.info("🐮googleRefreshToken: " + googleRefreshToken);
        log.info("username: {}, role: {}", username, role);

        if(googleRefreshToken != null) {
            eventPublisher.publishEvent(
                    new GoogleRefreshTokenListener(username, googleRefreshToken)
            );
        }
    }
}
