package com.example.bankcards.dto;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.UUID;

public record CreateCardRequest(
        String cardNumber,
        YearMonth expirationDate,
        BigDecimal balance,
        UUID ownerId
) {}