package com.example.bankcards.dto.response;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.UUID;

public record CardResponse(
        UUID id,
        String cardNumber,
        YearMonth expirationDate,
        CardResponse status,
        BigDecimal balance,
        UUID ownerId
) {}