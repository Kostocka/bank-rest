package com.example.bankcards.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record CardBlockRequestResponse(
        UUID id,
        UUID cardId,
        String status,
        LocalDateTime createdAt,
        LocalDateTime processedAt
) {}