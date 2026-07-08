package com.example.bankcards.dto.response;

import java.util.UUID;
import com.example.bankcards.entity.enums.RoleName;

public record UserResponse(
        UUID id,
        String username,
        RoleName role
) {}