package com.example.bankcards.service.card;

import java.util.UUID;
import com.example.bankcards.entity.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CardQueryService
{
    Card getCard(UUID cardId);

    Card getUserCard(UUID cardId, UUID userId);

    Page<Card> getCards(CardFilter filter, Pageable pageable);

    Page<Card> getUserCards(
            UUID userId,
            CardFilter filter,
            Pageable pageable
    );
}