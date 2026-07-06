package com.example.bankcards.service.card;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardBlockRequest;
import com.example.bankcards.entity.enums.BlockRequestStatus;
import com.example.bankcards.entity.enums.CardStatus;
import com.example.bankcards.exception.AccessDeniedException;
import com.example.bankcards.exception.BusinessException;
import com.example.bankcards.exception.NotFoundException;
import com.example.bankcards.repository.CardBlockRequestRepository;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.service.access.AccessService;
import com.example.bankcards.security.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class DefaultCardRequestService implements CardRequestService
{
    private final CardRepository cardRepository;
    private final CardBlockRequestRepository requestRepository;
    private final CurrentUserService currentUserService;
    private final AccessService accessService;

    @Override
    public CardBlockRequest createRequest(UUID cardId)
    {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new NotFoundException("Card not found"));

        if (!card.getOwner().getId().equals(currentUserService.getCurrentUserId()))
        {
            throw new AccessDeniedException("Access denied");
        }

        if (requestRepository.existsByCardIdAndStatus(cardId, BlockRequestStatus.PENDING))
        {
            throw new BusinessException("Request already exists");
        }

        CardBlockRequest request = new CardBlockRequest();
        request.setCard(card);
        request.setStatus(BlockRequestStatus.PENDING);
        request.setCreatedAt(LocalDateTime.now());

        return requestRepository.save(request);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CardBlockRequest> getRequests(Pageable pageable)
    {
        accessService.requireAdmin();
        return requestRepository.findAll(pageable);
    }

    @Override
    public CardBlockRequest approve(UUID requestId)
    {
        accessService.requireAdmin();

        CardBlockRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Request not found"));

        if (request.getStatus() != BlockRequestStatus.PENDING)
        {
            throw new BusinessException("Request already processed");
        }

        Card card = request.getCard();
        card.setStatus(CardStatus.BLOCKED);

        request.setStatus(BlockRequestStatus.APPROVED);
        request.setProcessedAt(LocalDateTime.now());

        cardRepository.save(card);

        return requestRepository.save(request);
    }

    @Override
    public CardBlockRequest reject(UUID requestId)
    {
        accessService.requireAdmin();

        CardBlockRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Request not found"));

        if (request.getStatus() != BlockRequestStatus.PENDING)
        {
            throw new BusinessException("Request already processed");
        }

        request.setStatus(BlockRequestStatus.REJECTED);
        request.setProcessedAt(LocalDateTime.now());

        return requestRepository.save(request);
    }
}