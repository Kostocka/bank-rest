package com.example.bankcards.dto;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.UUID;

public record CardResponse(
        UUID id,
        String cardNumber,
        YearMonth expirationDate,
        String status,
        BigDecimal balance,
        UUID ownerId
) {}