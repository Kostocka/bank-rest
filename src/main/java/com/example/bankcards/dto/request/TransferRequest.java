package com.example.bankcards.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.UUID;

public record TransferRequest(
        @NotNull(message = "Source card is required")
        UUID fromCardId,
        @NotNull(message = "Destination card is required")
        UUID toCardId,

        @NotNull(message = "Amount is required")
        @Positive(message = "Amount must be positive")
        BigDecimal amount
) {}