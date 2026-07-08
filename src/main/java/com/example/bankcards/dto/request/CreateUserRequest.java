package com.example.bankcards.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import com.example.bankcards.entity.enums.RoleName;

public record CreateUserRequest(
        @NotBlank(message = "Username is required")
        @Size(min = 3, max = 50)
        String username,

        @NotBlank(message = "Password is required")
        @Size(min = 6, max = 100)
        String password,

        @NotNull(message = "Role is required")
        RoleName role
) {}