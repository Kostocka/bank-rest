package com.example.bankcards.controller;

import jakarta.validation.Valid;
import com.example.bankcards.dto.request.TransferRequest;
import com.example.bankcards.security.CurrentUserService;
import com.example.bankcards.service.transfer.TransferCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transfers")
@RequiredArgsConstructor
public class TransferController
{
    private final TransferCommandService transferService;
    private final CurrentUserService currentUserService;

    @PostMapping
    public void transfer(@Valid @RequestBody TransferRequest transferRequest)
    {
        transferService.transfer(transferRequest, currentUserService.getCurrentUserId());
    }
}