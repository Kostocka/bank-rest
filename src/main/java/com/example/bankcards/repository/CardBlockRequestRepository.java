package com.example.bankcards.repository;

import java.util.UUID;
import com.example.bankcards.entity.CardBlockRequest;
import com.example.bankcards.entity.enums.BlockRequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardBlockRequestRepository extends JpaRepository<CardBlockRequest, UUID>
{
    boolean existsByCardIdAndStatus(UUID cardId, BlockRequestStatus status);

    Page<CardBlockRequest> findAllByStatus(BlockRequestStatus status, Pageable pageable);
}