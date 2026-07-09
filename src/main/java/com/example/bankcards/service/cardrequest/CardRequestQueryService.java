package com.example.bankcards.service.cardrequest;

import com.example.bankcards.entity.CardBlockRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CardRequestQueryService
{
    Page<CardBlockRequest> getRequests(Pageable pageable);
}