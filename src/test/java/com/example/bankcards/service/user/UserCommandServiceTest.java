package com.example.bankcards.service.user;

import com.example.bankcards.dto.request.CreateUserRequest;
import com.example.bankcards.dto.request.UpdateUserRequest;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import com.example.bankcards.entity.enums.RoleName;
import com.example.bankcards.exception.BusinessException;
import com.example.bankcards.repository.RoleRepository;
import com.example.bankcards.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserCommandServiceTest
{
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private UserCommandService service;

    @BeforeEach
    void setUp()
    {
        userRepository = mock(UserRepository.class);
        roleRepository = mock(RoleRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);

        service = new DefaultUserCommandService(
                userRepository,
                roleRepository,
                passwordEncoder
        );
    }

    @Test
    void shouldCreateUser()
    {
        CreateUserRequest request =
                new CreateUserRequest(
                        "kostya",
                        "password",
                        RoleName.ROLE_USER
                );

        Role role = new Role();
        role.setName(RoleName.ROLE_USER);

        when(userRepository.existsByUsername("kostya"))
                .thenReturn(false);
        when(roleRepository.findByName(RoleName.ROLE_USER))
                .thenReturn(Optional.of(role));
        when(passwordEncoder.encode("password"))
                .thenReturn("encoded");
        when(userRepository.save(any(User.class)))
                .thenAnswer(
                        i -> i.getArgument(0)
                );

        User result = service.create(request);

        assertEquals("kostya", result.getUsername());
        assertEquals("encoded", result.getPassword());
        assertEquals(role, result.getRole());
    }

    @Test
    void shouldThrowWhenUsernameExists()
    {
        CreateUserRequest request =
                new CreateUserRequest(
                        "admin",
                        "password",
                        RoleName.ROLE_ADMIN
                );

        when(userRepository.existsByUsername("admin"))
                .thenReturn(true);
        assertThrows(BusinessException.class, () -> service.create(request));
        verifyNoInteractions(roleRepository);
    }

    @Test
    void shouldUpdateUser()
    {
        UUID id = UUID.randomUUID();

        User user = new User();
        user.setUsername("old");

        UpdateUserRequest request =
                new UpdateUserRequest(
                        "new",
                        "password",
                        null
                );

        when(userRepository.findById(id))
                .thenReturn(Optional.of(user));
        when(userRepository.existsByUsername("new"))
                .thenReturn(false);
        when(passwordEncoder.encode("password"))
                .thenReturn("encoded");

        service.update(id, request);

        assertEquals(
                "new",
                user.getUsername()
        );

        assertEquals(
                "encoded",
                user.getPassword()
        );
    }

    @Test
    void shouldDeleteUser()
    {
        UUID id = UUID.randomUUID();

        User user = new User();

        when(userRepository.findById(id))
                .thenReturn(Optional.of(user));

        service.delete(id);

        verify(userRepository)
                .delete(user);
    }
}