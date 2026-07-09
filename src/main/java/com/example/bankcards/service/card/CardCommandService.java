package com.example.bankcards.service.card;

import java.util.UUID;
import com.example.bankcards.dto.request.CreateCardRequest;
import com.example.bankcards.entity.Card;

public interface CardCommandService
{
    Card create(CreateCardRequest request);

    void delete(UUID cardId);

    Card activate(UUID cardId);

    Card block(UUID cardId);
}