package com.example.bankcards.security;

import java.util.UUID;

public interface CurrentUserService
{
    UUID getCurrentUserId();

    boolean isAdmin();
}