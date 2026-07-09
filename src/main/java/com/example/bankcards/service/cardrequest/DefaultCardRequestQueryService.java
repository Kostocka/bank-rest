package com.example.bankcards.service.cardrequest;

import com.example.bankcards.entity.CardBlockRequest;
import com.example.bankcards.repository.CardBlockRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DefaultCardRequestQueryService implements CardRequestQueryService
{
    private final CardBlockRequestRepository repository;

    @Override
    public Page<CardBlockRequest> getRequests(Pageable pageable)
    {
        return repository.findAll(pageable);
    }
}