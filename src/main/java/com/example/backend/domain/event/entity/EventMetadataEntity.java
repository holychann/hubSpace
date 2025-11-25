package com.example.backend.domain.event.entity;

import com.example.backend.global.helper.StringListJsonConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "event_metadata")
public class EventMetadataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "event_id")
    private EventEntity event;

    @Column(name = "count")
    private Long count;

    @Column(name = "search_columns", columnDefinition = "jsonb")
    @Convert(converter = StringListJsonConverter.class)
    private List<String> searchColumns = new ArrayList<>();

    @Column(name = "search_columns_ids", columnDefinition = "jsonb")
    @Convert(converter = StringListJsonConverter.class)
    private List<String> searchColumnsIds = new ArrayList<>();

    @Column(name = "display_column")
    private String displayColumn;

    @Column(name = "form_id")
    private String formId;

    @Column(name = "form_url")
    private String formUrl;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
