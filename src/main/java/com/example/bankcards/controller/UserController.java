package com.example.bankcards.controller;

import com.example.bankcards.dto.CreateUserRequest;
import com.example.bankcards.dto.UserResponse;
import com.example.bankcards.service.user.UserService;
import com.example.bankcards.util.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController
{
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    public UserResponse create(@RequestBody CreateUserRequest createUserRequest)
    {
        return userMapper.toResponse(userService.createUser(createUserRequest));
    }

    @GetMapping("/{id}")
    public UserResponse get(@PathVariable UUID id)
    {
        return userMapper.toResponse(userService.getUser(id));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id)
    {
        userService.deleteUser(id);
    }
}