package com.example.bankcards.service.access;

import com.example.bankcards.entity.Card;

public interface AccessService
{
    void requireAdmin();

    void requireCardOwner(Card card);
}