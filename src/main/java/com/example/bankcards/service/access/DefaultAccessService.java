package com.example.bankcards.service.access;

import com.example.bankcards.entity.Card;
import com.example.bankcards.exception.AccessDeniedException;
import com.example.bankcards.security.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultAccessService implements AccessService
{
    private final CurrentUserService currentUserService;

    @Override
    public void requireAdmin()
    {
        if (!currentUserService.isAdmin())
        {
            throw new AccessDeniedException("Access denied");
        }
    }

    @Override
    public void requireCardOwner(Card card)
    {
        if (!card.getOwner().getId().equals(currentUserService.getCurrentUserId()))
        {
            throw new AccessDeniedException("Access denied");
        }
    }
}