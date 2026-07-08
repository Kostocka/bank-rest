package com.example.bankcards.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;
import com.example.bankcards.entity.enums.BlockRequestStatus;

public record CardBlockRequestResponse(
        UUID id,
        UUID cardId,
        BlockRequestStatus status,
        LocalDateTime createdAt,
        LocalDateTime processedAt
) {}