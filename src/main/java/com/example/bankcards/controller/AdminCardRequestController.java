package com.example.bankcards.controller;

import java.util.UUID;
import com.example.bankcards.dto.response.CardBlockRequestResponse;
import com.example.bankcards.service.cardrequest.CardRequestCommandService;
import com.example.bankcards.service.cardrequest.CardRequestQueryService;
import com.example.bankcards.util.mapper.CardBlockRequestMapper;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/card-requests")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminCardRequestController
{
    private final CardRequestQueryService queryService;
    private final CardRequestCommandService commandService;
    private final CardBlockRequestMapper mapper;

    @GetMapping
    public Page<CardBlockRequestResponse> getAll(@ParameterObject Pageable pageable)
    {
        return queryService
                .getRequests(pageable)
                .map(mapper::toResponse);
    }

    @PostMapping("/{id}/approve")
    public CardBlockRequestResponse approve(@PathVariable UUID id)
    {
        return mapper.toResponse(
                commandService.approve(id)
        );
    }

    @PostMapping("/{id}/reject")
    public CardBlockRequestResponse reject(@PathVariable UUID id)
    {
        return mapper.toResponse(
                commandService.reject(id)
        );
    }
}