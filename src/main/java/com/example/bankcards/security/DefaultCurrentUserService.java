package com.example.bankcards.security;

import java.util.UUID;
import com.example.bankcards.exception.BusinessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class DefaultCurrentUserService implements CurrentUserService
{
    @Override
    public UUID getCurrentUserId()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails user))
        {
            throw new BusinessException("User is not authenticated");
        }

        return user.getUser().getId();
    }

    @Override
    public boolean isAdmin()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null)
        {
            return false;
        }

        return authentication.getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }
}