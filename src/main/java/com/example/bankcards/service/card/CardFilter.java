package com.example.bankcards.service.card;

import com.example.bankcards.entity.enums.CardStatus;
import java.math.BigDecimal;
import java.time.YearMonth;

public record CardFilter(
        CardStatus status,
        YearMonth expirationDate,
        BigDecimal minBalance,
        BigDecimal maxBalance
) { }