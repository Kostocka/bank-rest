package com.example.bankcards.service.transfer;

import com.example.bankcards.dto.TransferRequest;

public interface TransferService
{
    void transfer(TransferRequest request);
}