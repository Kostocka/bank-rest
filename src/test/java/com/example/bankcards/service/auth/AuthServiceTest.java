package com.example.bankcards.service.auth;

import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import com.example.bankcards.entity.enums.RoleName;
import com.example.bankcards.dto.request.LoginRequest;
import com.example.bankcards.exception.BusinessException;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest
{
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JwtService jwtService;
    private AuthService service;

    @BeforeEach
    void setUp()
    {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        jwtService = mock(JwtService.class);

        service = new DefaultAuthService(
                userRepository,
                passwordEncoder,
                jwtService
        );
    }

    @Test
    void shouldLoginSuccessfully()
    {
        LoginRequest request =
                new LoginRequest(
                        "admin",
                        "password"
                );

        Role role = new Role();
        role.setName(RoleName.ROLE_ADMIN);

        User user = new User();
        user.setUsername("admin");
        user.setPassword("encoded");
        user.setRole(role);

        when(userRepository.findByUsername("admin"))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", "encoded"))
                .thenReturn(true);
        when(jwtService.generateToken("admin", List.of("ROLE_ADMIN")))
                .thenReturn("jwt-token");

        String result = service.login(request);

        assertEquals("jwt-token", result);

        verify(jwtService)
                .generateToken(
                        "admin",
                        List.of("ROLE_ADMIN")
                );
    }

    @Test
    void shouldThrowWhenUserNotFound()
    {
        LoginRequest request =
                new LoginRequest(
                        "unknown",
                        "password"
                );

        when(userRepository.findByUsername("unknown"))
                .thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> service.login(request));

        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void shouldThrowWhenPasswordInvalid()
    {
        LoginRequest request =
                new LoginRequest(
                        "admin",
                        "wrong"
                );

        User user = new User();
        user.setPassword("encoded");

        when(userRepository.findByUsername("admin"))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "encoded"))
                .thenReturn(false);

        assertThrows(BusinessException.class, () -> service.login(request));

        verifyNoInteractions(jwtService);
    }
}