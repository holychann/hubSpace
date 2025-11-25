package com.example.backend.domain.event.repository;

import com.example.backend.domain.event.entity.EventMetadataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventMetadataRepository extends JpaRepository<EventMetadataEntity, Long> {
}
