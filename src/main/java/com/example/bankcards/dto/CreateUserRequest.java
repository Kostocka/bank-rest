package com.example.bankcards.dto;

import com.example.bankcards.entity.enums.RoleName;

public record CreateUserRequest(
        String username,
        String password,
        RoleName role
) {}