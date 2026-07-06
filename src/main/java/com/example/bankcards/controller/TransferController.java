package com.example.bankcards.controller;

import jakarta.validation.Valid;
import com.example.bankcards.dto.TransferRequest;
import com.example.bankcards.service.transfer.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transfers")
@RequiredArgsConstructor
public class TransferController
{
    private final TransferService transferService;

    @PostMapping
    public void transfer(@Valid @RequestBody TransferRequest transferRequest)
    {
        transferService.transfer(transferRequest);
    }
}