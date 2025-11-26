package com.example.backend.domain.event.repository.query;

import com.example.backend.domain.event.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventQueryRepository extends JpaRepository<EventEntity, Long>, EventQueryRepositoryDsl {
    Boolean existsByEventId(Long eventId);
}
