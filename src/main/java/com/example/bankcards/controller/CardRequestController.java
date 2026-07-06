package com.example.bankcards.controller;

import java.util.UUID;
import com.example.bankcards.dto.CardBlockRequestResponse;
import com.example.bankcards.service.card.CardRequestService;
import com.example.bankcards.util.mapper.CardBlockRequestMapper;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/card-requests")
@RequiredArgsConstructor
public class CardRequestController
{
    private final CardRequestService service;
    private final CardBlockRequestMapper mapper;

    @PostMapping("/{cardId}")
    public CardBlockRequestResponse create(@PathVariable UUID cardId)
    {
        return mapper.toResponse(service.createRequest(cardId));
    }

    @GetMapping
    public Page<CardBlockRequestResponse> getAll(@ParameterObject Pageable pageable)
    {
        return service.getRequests(pageable)
                .map(mapper::toResponse);
    }

    @PostMapping("/{id}/approve")
    public CardBlockRequestResponse approve(@PathVariable UUID id)
    {
        return mapper.toResponse(service.approve(id));
    }

    @PostMapping("/{id}/reject")
    public CardBlockRequestResponse reject(@PathVariable UUID id)
    {
        return mapper.toResponse(service.reject(id));
    }
}