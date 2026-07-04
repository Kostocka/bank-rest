package com.example.bankcards.util.mapper;

import com.example.bankcards.dto.CardResponse;
import com.example.bankcards.entity.Card;
import com.example.bankcards.util.CardNumberMasker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CardMapper
{
    private final CardNumberMasker masker;

    public CardResponse toResponse(Card card)
    {
        return new CardResponse(
                card.getId(),
                masker.mask(card.getCardNumber()),
                card.getExpirationDate(),
                card.getStatus().name(),
                card.getBalance(),
                card.getOwner().getId()
        );
    }
}