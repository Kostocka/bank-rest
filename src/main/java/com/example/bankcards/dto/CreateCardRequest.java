package com.example.bankcards.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.UUID;

public record CreateCardRequest(
        @NotBlank(message = "Card number is required")
        String cardNumber,

        @NotNull(message = "Expiration date is required")
        YearMonth expirationDate,

        @PositiveOrZero(message = "Balance cannot be negative")
        BigDecimal balance,

        @NotNull(message = "Owner id is required")
        UUID ownerId
) {}