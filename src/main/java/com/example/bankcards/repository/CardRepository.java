package com.example.bankcards.repository;

import java.util.Optional;
import java.util.UUID;
import com.example.bankcards.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CardRepository extends
        JpaRepository<Card, UUID>,
        JpaSpecificationExecutor<Card>
{
    boolean existsByCardNumber(String cardNumber);

    Optional<Card> findByIdAndOwnerId(UUID cardId, UUID ownerId);
}