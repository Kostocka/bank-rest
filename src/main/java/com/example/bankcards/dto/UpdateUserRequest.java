package com.example.bankcards.dto;

import com.example.bankcards.entity.enums.RoleName;

public record UpdateUserRequest(
        String username,
        String password,
        RoleName role
) {}