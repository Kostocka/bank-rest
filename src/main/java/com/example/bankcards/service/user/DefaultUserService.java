package com.example.bankcards.service.user;

import java.util.UUID;
import com.example.bankcards.dto.CreateUserRequest;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import com.example.bankcards.entity.enums.RoleName;
import com.example.bankcards.repository.RoleRepository;
import com.example.bankcards.repository.UserRepository;
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

    @Override
    public User createUser(CreateUserRequest req)
    {
        Role role = roleRepository.findByName(req.role())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        User user = new User();
        user.setUsername(req.username());
        user.setPassword(passwordEncoder.encode(req.password()));
        user.setRole(role);

        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user)
    {
        User existing = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getUsername() != null &&
                !user.getUsername().isBlank() &&
                !user.getUsername().equals(existing.getUsername()))
        {

            if (userRepository.existsByUsername(user.getUsername()))
            {
                throw new RuntimeException("Username already taken");
            }

            existing.setUsername(user.getUsername());
        }

        if (user.getPassword() != null)
        {
            existing.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        if (user.getRole() != null)
        {
            existing.setRole(user.getRole());
        }

        return userRepository.save(existing);
    }

    @Override
    public void deleteUser(UUID userId)
    {
        userRepository.deleteById(userId);
    }

    @Override
    public User getUser(UUID userId)
    {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public Page<User> getUsers(Pageable pageable)
    {
        return userRepository.findAll(pageable);
    }
}