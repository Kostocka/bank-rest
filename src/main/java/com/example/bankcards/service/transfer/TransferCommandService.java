package com.example.bankcards.service.transfer;

import java.util.UUID;
import com.example.bankcards.dto.request.TransferRequest;

public interface TransferCommandService
{
    void transfer(TransferRequest request, UUID userId);
}