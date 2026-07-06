package com.example.bankcards.util.mapper;

import com.example.bankcards.dto.CardBlockRequestResponse;
import com.example.bankcards.entity.CardBlockRequest;
import org.springframework.stereotype.Component;

@Component
public class CardBlockRequestMapper
{
    public CardBlockRequestResponse toResponse(CardBlockRequest request)
    {
        return new CardBlockRequestResponse(
                request.getId(),
                request.getCard().getId(),
                request.getStatus().name(),
                request.getCreatedAt(),
                request.getProcessedAt()
        );
    }
}