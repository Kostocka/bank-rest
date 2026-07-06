package com.example.bankcards.util.mapper;

import com.example.bankcards.dto.CardResponse;
import com.example.bankcards.entity.Card;
import com.example.bankcards.util.CardEncryptor;
import com.example.bankcards.util.CardNumberMasker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CardMapper
{
    private final CardEncryptor encryptor;
    private final CardNumberMasker masker;

    public CardResponse toResponse(Card card)
    {
        String cardNumber = encryptor.decrypt(card.getCardNumber());

        return new CardResponse(
                card.getId(),
                masker.mask(cardNumber),
                card.getExpirationDate(),
                card.getStatus().name(),
                card.getBalance(),
                card.getOwner().getId()
        );
    }
}