package com.example.bankcards.controller;

import com.example.bankcards.dto.CardResponse;
import com.example.bankcards.dto.CreateCardRequest;
import com.example.bankcards.service.card.CardService;
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
    private final CardService cardService;
    private final CardMapper cardMapper;

    @PostMapping
    public CardResponse create(@RequestBody CreateCardRequest request)
    {
        return cardMapper.toResponse(cardService.createCard(request));
    }

    @GetMapping("/{id}")
    public CardResponse get(@PathVariable UUID id)
    {
        return cardMapper.toResponse(cardService.getCard(id));
    }

    @GetMapping
    public Page<CardResponse> getAll(@ParameterObject Pageable pageable)
    {
        return cardService.getCards(null, pageable).map(cardMapper::toResponse);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id)
    {
        cardService.deleteCard(id);
    }

    @PostMapping("/{id}/activate")
    public CardResponse activate(@PathVariable UUID id)
    {
        return cardMapper.toResponse(cardService.activateCard(id));
    }

    @PostMapping("/{id}/block")
    public CardResponse block(@PathVariable UUID id)
    {
        return cardMapper.toResponse(cardService.blockCard(id));
    }
}