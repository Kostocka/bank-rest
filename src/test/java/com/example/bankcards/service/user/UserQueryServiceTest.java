package com.example.bankcards.service.user;

import com.example.bankcards.entity.User;
import com.example.bankcards.exception.NotFoundException;
import com.example.bankcards.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserQueryServiceTest
{
    private UserRepository repository;
    private UserQueryService service;

    @BeforeEach
    void setUp()
    {
        repository = mock(UserRepository.class);
        service = new DefaultUserQueryService(repository);
    }

    @Test
    void shouldGetUser()
    {
        UUID id = UUID.randomUUID();
        User user = new User();

        when(repository.findById(id))
                .thenReturn(Optional.of(user));

        User result = service.get(id);

        assertEquals(user, result);
    }

    @Test
    void shouldThrowWhenUserNotFound()
    {
        UUID id = UUID.randomUUID();

        when(repository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.get(id));
    }
}