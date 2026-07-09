package com.example.bankcards.controller;

import jakarta.validation.Valid;
import java.util.UUID;
import com.example.bankcards.dto.request.CreateCardRequest;
import com.example.bankcards.dto.response.CardResponse;
import com.example.bankcards.service.card.CardCommandService;
import com.example.bankcards.service.card.CardFilter;
import com.example.bankcards.service.card.CardQueryService;
import com.example.bankcards.util.mapper.CardMapper;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/cards")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminCardController
{
    private final CardCommandService commandService;
    private final CardQueryService queryService;
    private final CardMapper mapper;

    @GetMapping("/{id}")
    public CardResponse get(@PathVariable UUID id)
    {
        return mapper.toResponse(
                queryService.getCard(id)
        );
    }

    @GetMapping
    public Page<CardResponse> getAll(@ParameterObject CardFilter filter, @ParameterObject Pageable pageable)
    {
        return queryService.getCards(filter, pageable)
                .map(mapper::toResponse);
    }


    @PostMapping
    public CardResponse create(@Valid @RequestBody CreateCardRequest request)
    {
        return mapper.toResponse(
                commandService.create(request)
        );
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id)
    {
        commandService.delete(id);
    }

    @PostMapping("/{id}/activate")
    public CardResponse activate(@PathVariable UUID id)
    {
        return mapper.toResponse(
                commandService.activate(id)
        );
    }

    @PostMapping("/{id}/block")
    public CardResponse block(@PathVariable UUID id)
    {
        return mapper.toResponse(
                commandService.block(id)
        );
    }
}