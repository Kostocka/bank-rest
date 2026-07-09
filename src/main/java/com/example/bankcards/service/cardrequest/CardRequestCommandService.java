package com.example.bankcards.service.cardrequest;

import java.util.UUID;
import com.example.bankcards.entity.CardBlockRequest;

public interface CardRequestCommandService
{
    CardBlockRequest createRequest(UUID cardId, UUID userId);

    CardBlockRequest approve(UUID requestId);

    CardBlockRequest reject(UUID requestId);
}