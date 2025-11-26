package com.example.backend.domain.event.repository.query;

import com.example.backend.domain.event.entity.EventEntity;
import com.example.backend.domain.user.entity.UserEntity;

import java.util.List;

public interface EventQueryRepositoryDsl {
    List<EventEntity> findByUserIdAndIsActive(UserEntity userEntity, Boolean isActive);
}
