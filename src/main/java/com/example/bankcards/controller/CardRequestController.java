package com.example.bankcards.controller;

import java.util.UUID;
import com.example.bankcards.dto.response.CardBlockRequestResponse;
import com.example.bankcards.security.CurrentUserService;
import com.example.bankcards.service.cardrequest.CardRequestCommandService;
import com.example.bankcards.util.mapper.CardBlockRequestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cards")
@RequiredArgsConstructor
public class CardRequestController
{
    private final CardRequestCommandService commandService;
    private final CurrentUserService currentUser;
    private final CardBlockRequestMapper mapper;

    @PostMapping("/{id}/block-request")
    public CardBlockRequestResponse requestBlock(@PathVariable UUID id)
    {
        return mapper.toResponse(
                commandService.createRequest(id, currentUser.getCurrentUserId())
        );
    }
}