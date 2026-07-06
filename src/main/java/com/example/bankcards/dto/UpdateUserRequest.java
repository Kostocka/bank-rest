package com.example.bankcards.dto;

import jakarta.validation.constraints.Size;
import com.example.bankcards.entity.enums.RoleName;

public record UpdateUserRequest(
        @Size(min = 3, max = 50)
        String username,

        @Size(min = 6, max = 50)
        String password,

        RoleName role
) {}