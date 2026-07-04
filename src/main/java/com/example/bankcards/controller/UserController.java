package com.example.bankcards.controller;

import com.example.bankcards.dto.CreateUserRequest;
import com.example.bankcards.entity.User;
import com.example.bankcards.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController
{
    private final UserService userService;

    @PostMapping
    public User create(@RequestBody CreateUserRequest createUserRequest)
    {
        return userService.createUser(createUserRequest);
    }

    @GetMapping("/{id}")
    public User get(@PathVariable UUID id)
    {
        return userService.getUser(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id)
    {
        userService.deleteUser(id);
    }
}