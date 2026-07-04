package com.example.bankcards.controller;

import com.example.bankcards.dto.CreateCardRequest;
import com.example.bankcards.entity.Card;
import com.example.bankcards.service.card.CardService;
import lombok.RequiredArgsConstructor;
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

    @PostMapping
    public Card create(@RequestBody CreateCardRequest request)
    {
        return cardService.createCard(request);
    }

    @GetMapping("/{id}")
    public Card get(@PathVariable UUID id)
    {
        return cardService.getCard(id);
    }

    @GetMapping
    public Page<Card> getAll(Pageable pageable)
    {
        return cardService.getCards(null, pageable);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id)
    {
        cardService.deleteCard(id);
    }

    @PostMapping("/{id}/activate")
    public Card activate(@PathVariable UUID id)
    {
        return cardService.activateCard(id);
    }

    @PostMapping("/{id}/block")
    public Card block(@PathVariable UUID id)
    {
        return cardService.blockCard(id);
    }
}