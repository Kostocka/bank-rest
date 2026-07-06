package com.example.bankcards.service.user;

import java.util.UUID;
import com.example.bankcards.dto.CreateUserRequest;
import com.example.bankcards.dto.UpdateUserRequest;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.BusinessException;
import com.example.bankcards.exception.NotFoundException;
import com.example.bankcards.repository.RoleRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.access.AccessService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultUserService implements UserService
{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccessService accessService;

    @Override
    public User createUser(CreateUserRequest req)
    {
        accessService.requireAdmin();
        Role role = roleRepository.findByName(req.role())
                .orElseThrow(() -> new NotFoundException("Role not found"));

        if (userRepository.existsByUsername(req.username()))
        {
            throw new BusinessException("Username already taken");
        }

        User user = new User();
        user.setUsername(req.username());
        user.setPassword(passwordEncoder.encode(req.password()));
        user.setRole(role);

        return userRepository.save(user);
    }

    @Override
    public User updateUser(UpdateUserRequest request)
    {
        accessService.requireAdmin();
        User existing = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (request.username() != null &&
                !request.username().equals(existing.getUsername()))
        {
            if (userRepository.existsByUsername(request.username()))
            {
                throw new BusinessException("Username already taken");
            }

            existing.setUsername(request.username());
        }

        if (request.password() != null)
        {
            existing.setPassword(passwordEncoder.encode(request.password()));
        }

        if (request.role() != null)
        {
            Role role = roleRepository.findByName(request.role())
                    .orElseThrow(() -> new NotFoundException("Role not found"));
            existing.setRole(role);
        }

        return userRepository.save(existing);
    }

    @Override
    public void deleteUser(UUID userId)
    {
        accessService.requireAdmin();
        userRepository.deleteById(userId);
    }

    @Override
    public User getUser(UUID userId)
    {
        accessService.requireAdmin();
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Override
    public Page<User> getUsers(Pageable pageable)
    {
        accessService.requireAdmin();
        return userRepository.findAll(pageable);
    }
}