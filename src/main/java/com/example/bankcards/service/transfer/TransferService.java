package com.example.bankcards.service.transfer;

import java.math.BigDecimal;
import java.util.UUID;

public interface TransferService
{
    void transfer(UUID fromCardId, UUID toCardId, BigDecimal amount);
}