package com.example.bankcards.repository;

import java.util.Optional;
import java.util.UUID;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, UUID>
{
    Optional<Role> findByName(RoleName name);
}