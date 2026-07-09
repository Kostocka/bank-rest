package com.example.bankcards.service.user;

import java.util.UUID;
import com.example.bankcards.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserQueryService
{
    User get(UUID userId);

    Page<User> getAll(Pageable pageable);
}