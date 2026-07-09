package com.example.bankcards.controller;

import jakarta.validation.Valid;
import java.util.UUID;
import com.example.bankcards.dto.request.CreateUserRequest;
import com.example.bankcards.dto.request.UpdateUserRequest;
import com.example.bankcards.dto.response.UserResponse;
import com.example.bankcards.service.user.UserCommandService;
import com.example.bankcards.service.user.UserQueryService;
import com.example.bankcards.util.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController
{
    private final UserCommandService commandService;
    private final UserQueryService queryService;
    private final UserMapper mapper;

    @PostMapping
    public UserResponse create(@Valid @RequestBody CreateUserRequest request)
    {
        return mapper.toResponse(
                commandService.create(request)
        );
    }

    @PutMapping("/{id}")
    public UserResponse update(@PathVariable UUID id ,@Valid @RequestBody UpdateUserRequest request)
    {
        return mapper.toResponse(
                commandService.update(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id)
    {
        commandService.delete(id);
    }

    @GetMapping("/{id}")
    public UserResponse get(@PathVariable UUID id)
    {
        return mapper.toResponse(
                queryService.get(id)
        );
    }

    @GetMapping
    public Page<UserResponse> getAll(@ParameterObject Pageable pageable)
    {
        return queryService
                .getAll(pageable)
                .map(mapper::toResponse);
    }
}