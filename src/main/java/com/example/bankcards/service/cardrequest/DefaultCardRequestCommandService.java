package com.example.bankcards.service.cardrequest;

import java.time.LocalDateTime;
import java.util.UUID;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardBlockRequest;
import com.example.bankcards.entity.enums.BlockRequestStatus;
import com.example.bankcards.entity.enums.CardStatus;
import com.example.bankcards.exception.BusinessException;
import com.example.bankcards.exception.NotFoundException;
import com.example.bankcards.repository.CardBlockRequestRepository;
import com.example.bankcards.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DefaultCardRequestCommandService implements CardRequestCommandService
{
    private final CardRepository cardRepository;
    private final CardBlockRequestRepository requestRepository;

    @Override
    public CardBlockRequest createRequest(UUID cardId, UUID userId)
    {
        Card card = cardRepository
                .findByIdAndOwnerId(cardId, userId)
                .orElseThrow(() -> new NotFoundException("Card not found"));


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
    public CardBlockRequest approve(UUID requestId)
    {
        CardBlockRequest request = getPendingRequest(requestId);

        Card card = request.getCard();
        card.setStatus(CardStatus.BLOCKED);

        request.setStatus(BlockRequestStatus.APPROVED);
        request.setProcessedAt(LocalDateTime.now());

        return requestRepository.save(request);
    }

    @Override
    public CardBlockRequest reject(UUID requestId)
    {
        CardBlockRequest request = getPendingRequest(requestId);

        request.setStatus(BlockRequestStatus.REJECTED);
        request.setProcessedAt(LocalDateTime.now());

        return requestRepository.save(request);
    }

    private CardBlockRequest getPendingRequest(UUID requestId)
    {
        CardBlockRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Request not found"));

        if(request.getStatus() != BlockRequestStatus.PENDING)
        {
            throw new BusinessException("Request already processed");
        }

        return request;
    }
}