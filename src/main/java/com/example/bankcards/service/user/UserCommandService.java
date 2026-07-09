package com.example.bankcards.service.user;

import java.util.UUID;
import com.example.bankcards.dto.request.CreateUserRequest;
import com.example.bankcards.dto.request.UpdateUserRequest;
import com.example.bankcards.entity.User;

public interface UserCommandService
{
    User create(CreateUserRequest request);

    User update(UUID userId,UpdateUserRequest request);

    void delete(UUID userId);
}