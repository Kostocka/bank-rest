package com.example.bankcards.controller;

import com.example.bankcards.dto.CreateUserRequest;
import com.example.bankcards.dto.UpdateUserRequest;
import com.example.bankcards.dto.UserResponse;
import com.example.bankcards.service.user.UserService;
import com.example.bankcards.util.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @PutMapping("/{id}")
    public UserResponse update(@PathVariable UUID id, @RequestBody UpdateUserRequest request)
    {
        return userMapper.toResponse(userService.updateUser(request));
    }

    @GetMapping("/{id}")
    public UserResponse get(@PathVariable UUID id)
    {
        return userMapper.toResponse(userService.getUser(id));
    }

    @GetMapping
    public Page<UserResponse> getAll(@ParameterObject Pageable pageable)
    {
        return userService.getUsers(pageable).map(userMapper::toResponse);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id)
    {
        userService.deleteUser(id);
    }
}