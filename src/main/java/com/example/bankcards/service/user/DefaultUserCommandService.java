package com.example.bankcards.service.user;

import java.util.UUID;
import com.example.bankcards.dto.request.CreateUserRequest;
import com.example.bankcards.dto.request.UpdateUserRequest;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.BusinessException;
import com.example.bankcards.exception.NotFoundException;
import com.example.bankcards.repository.RoleRepository;
import com.example.bankcards.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DefaultUserCommandService implements UserCommandService
{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User create(CreateUserRequest request)
    {
        if (userRepository.existsByUsername(request.username()))
        {
            throw new BusinessException("Username already taken");
        }

        Role role = roleRepository.findByName(request.role())
                .orElseThrow(() -> new NotFoundException("Role not found"));

        User user = new User();
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(role);

        return userRepository.save(user);
    }

    @Override
    public User update(UUID userId, UpdateUserRequest request)
    {
        User existing = userRepository.findById(userId)
                        .orElseThrow(() -> new NotFoundException("User not found"));

        if(request.username() != null && !request.username().equals(existing.getUsername()))
        {
            if(userRepository.existsByUsername(request.username()))
            {
                throw new BusinessException("Username already taken");
            }

            existing.setUsername(request.username());
        }

        if(request.password() != null)
        {
            existing.setPassword(passwordEncoder.encode(request.password()));
        }

        if(request.role() != null)
        {
            Role role = roleRepository.findByName(request.role())
                            .orElseThrow(() -> new NotFoundException("Role not found"));

            existing.setRole(role);
        }

        return userRepository.save(existing);
    }

    @Override
    public void delete(UUID userId)
    {
        User user =
                userRepository.findById(userId)
                        .orElseThrow(() -> new NotFoundException("User not found"));

        userRepository.delete(user);
    }
}