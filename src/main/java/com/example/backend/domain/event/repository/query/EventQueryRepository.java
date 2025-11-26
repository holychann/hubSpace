package com.example.backend.domain.event.repository.query;

import com.example.backend.domain.event.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventQueryRepository extends JpaRepository<EventEntity, Long>, EventQueryRepositoryDsl {

}
