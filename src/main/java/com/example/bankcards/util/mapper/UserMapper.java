package com.example.bankcards.util.mapper;

import com.example.bankcards.dto.response.UserResponse;
import com.example.bankcards.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper
{
    public UserResponse toResponse(User user)
    {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getRole().getName().name()
        );
    }
}