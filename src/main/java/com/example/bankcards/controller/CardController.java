package com.example.bankcards.controller;

import com.example.bankcards.dto.response.CardResponse;
import com.example.bankcards.security.CurrentUserService;
import com.example.bankcards.service.card.CardQueryService;
import com.example.bankcards.util.mapper.CardMapper;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/cards")
@RequiredArgsConstructor
public class CardController
{
    private final CardQueryService queryService;
    private final CurrentUserService currentUser;
    private final CardMapper mapper;

    @GetMapping("/{id}")
    public CardResponse get(@PathVariable UUID id)
    {
        return mapper.toResponse(
                queryService.getUserCard(id, currentUser.getCurrentUserId()));
    }

    @GetMapping
    public Page<CardResponse> getAll(@ParameterObject Pageable pageable)
    {
        return queryService
                .getUserCards(currentUser.getCurrentUserId(), null, pageable)
                .map(mapper::toResponse);
    }
}