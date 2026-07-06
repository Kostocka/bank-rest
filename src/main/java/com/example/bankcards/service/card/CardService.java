package com.example.bankcards.service.card;

import com.example.bankcards.dto.CreateCardRequest;
import com.example.bankcards.entity.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.util.UUID;

public interface CardService
{
    Card createCard(CreateCardRequest req);

    Card activateCard(UUID cardId);

    Card blockCard(UUID cardId);

    void deleteCard(UUID cardId);

    Card getCard(UUID cardId);

    Page<Card> getCards(
            CardFilter filter,
            Pageable pageable
    );

    BigDecimal getBalance(UUID cardId);
}