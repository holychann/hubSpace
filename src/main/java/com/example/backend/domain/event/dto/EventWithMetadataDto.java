package com.example.backend.domain.event.dto;

import com.example.backend.domain.event.entity.EventEntity;
import com.example.backend.domain.event.entity.EventMetadataEntity;

public record EventWithMetadataDto(EventEntity event, EventMetadataEntity metadata) {
}
