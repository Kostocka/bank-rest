package com.example.bankcards.service.card;

import java.util.UUID;
import com.example.bankcards.entity.CardBlockRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CardRequestService
{
    CardBlockRequest createRequest(UUID cardId);

    Page<CardBlockRequest> getRequests(Pageable pageable);

    CardBlockRequest approve(UUID requestId);

    CardBlockRequest reject(UUID requestId);
}