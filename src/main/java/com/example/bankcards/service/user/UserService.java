package com.example.bankcards.service.user;

import com.example.bankcards.dto.CreateUserRequest;
import com.example.bankcards.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface UserService
{
    User createUser(CreateUserRequest req);

    User updateUser(User user);

    void deleteUser(UUID userId);

    User getUser(UUID userId);

    Page<User> getUsers(Pageable pageable);
}