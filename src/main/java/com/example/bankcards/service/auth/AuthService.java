package com.example.bankcards.service.auth;

import com.example.bankcards.dto.LoginRequest;

public interface AuthService
{
    String login(LoginRequest request);
}