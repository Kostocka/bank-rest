package com.example.bankcards.dto.response;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.UUID;
import com.example.bankcards.entity.enums.CardStatus;

public record CardResponse(
        UUID id,
        String cardNumber,
        YearMonth expirationDate,
        CardStatus status,
        BigDecimal balance,
        UUID ownerId
) {}