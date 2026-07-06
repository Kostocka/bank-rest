package com.example.bankcards.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import com.example.bankcards.entity.enums.BlockRequestStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "card_block_requests")
@Getter
@Setter
@NoArgsConstructor
public class CardBlockRequest
{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BlockRequestStatus status;

    @Column(nullable =false)
    private LocalDateTime createdAt;

    private LocalDateTime processedAt;
}