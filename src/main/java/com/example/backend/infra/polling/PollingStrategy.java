package com.example.backend.infra.polling;

import com.example.backend.domain.event.entity.EventEntity;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class PollingStrategy {

    public Duration calculateInterval(EventEntity event) {
        long days = ChronoUnit.DAYS.between(event.getCreatedAt(), LocalDateTime.now());

        if (days <= 7) return Duration.ofMinutes(1);
        if (days <= 10) return Duration.ofHours(1);
        if (days <= 30) return Duration.ofHours(6);

        return Duration.ZERO; // 종료 구간
    }
}
