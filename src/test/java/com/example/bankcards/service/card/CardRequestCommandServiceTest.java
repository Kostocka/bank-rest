package com.example.bankcards.service.card;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardBlockRequest;
import com.example.bankcards.entity.enums.BlockRequestStatus;
import com.example.bankcards.entity.enums.CardStatus;
import com.example.bankcards.exception.BusinessException;
import com.example.bankcards.exception.NotFoundException;
import com.example.bankcards.repository.CardBlockRequestRepository;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.service.cardrequest.CardRequestCommandService;
import com.example.bankcards.service.cardrequest.DefaultCardRequestCommandService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CardRequestCommandServiceTest
{
    private CardRepository cardRepository;
    private CardBlockRequestRepository requestRepository;
    private CardRequestCommandService service;

    @BeforeEach
    void setUp()
    {
        cardRepository = mock(CardRepository.class);
        requestRepository = mock(CardBlockRequestRepository.class);
        service = new DefaultCardRequestCommandService(cardRepository, requestRepository);
    }

    @Test
    void shouldCreateRequest()
    {
        UUID cardId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Card card = new Card();
        card.setId(cardId);

        when(cardRepository.findByIdAndOwnerId(cardId, userId))
                .thenReturn(Optional.of(card));
        when(requestRepository.existsByCardIdAndStatus(cardId, BlockRequestStatus.PENDING))
                .thenReturn(false);
        when(requestRepository.save(any(CardBlockRequest.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CardBlockRequest result = service.createRequest(cardId, userId);

        assertEquals(BlockRequestStatus.PENDING, result.getStatus());
        assertEquals(card, result.getCard());

        verify(requestRepository)
                .save(any(CardBlockRequest.class));
    }

    @Test
    void shouldNotCreateRequestForAnotherUserCard()
    {
        UUID cardId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        when(cardRepository.findByIdAndOwnerId(cardId, userId))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.createRequest(cardId, userId));

        verify(requestRepository, never())
                .save(any());
    }

    @Test
    void shouldNotCreateDuplicateRequest()
    {
        UUID cardId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Card card = new Card();

        when(cardRepository.findByIdAndOwnerId(cardId, userId))
                .thenReturn(Optional.of(card));
        when(requestRepository.existsByCardIdAndStatus(cardId, BlockRequestStatus.PENDING))
                .thenReturn(true);

        assertThrows(BusinessException.class, () -> service.createRequest(cardId, userId));

        verify(requestRepository, never())
                .save(any());
    }

    @Test
    void shouldApproveRequest()
    {
        UUID requestId = UUID.randomUUID();

        Card card = new Card();
        card.setStatus(CardStatus.ACTIVE);

        CardBlockRequest request = new CardBlockRequest();
        request.setCard(card);
        request.setStatus(BlockRequestStatus.PENDING);

        when(requestRepository.findById(requestId))
                .thenReturn(Optional.of(request));
        when(requestRepository.save(request))
                .thenReturn(request);

        CardBlockRequest result = service.approve(requestId);

        assertEquals(BlockRequestStatus.APPROVED, result.getStatus());
        assertEquals(CardStatus.BLOCKED, card.getStatus());

        verify(requestRepository)
                .save(request);
    }

    @Test
    void shouldRejectRequest()
    {
        UUID requestId = UUID.randomUUID();
        CardBlockRequest request = new CardBlockRequest();
        request.setStatus(BlockRequestStatus.PENDING);

        when(requestRepository.findById(requestId))
                .thenReturn(Optional.of(request));
        when(requestRepository.save(request))
                .thenReturn(request);

        CardBlockRequest result = service.reject(requestId);

        assertEquals(BlockRequestStatus.REJECTED, result.getStatus());

        verify(requestRepository)
                .save(request);
    }

    @Test
    void shouldNotApproveProcessedRequest()
    {
        UUID requestId = UUID.randomUUID();
        CardBlockRequest request = new CardBlockRequest();
        request.setStatus(BlockRequestStatus.REJECTED);

        when(requestRepository.findById(requestId))
                .thenReturn(Optional.of(request));

        assertThrows(BusinessException.class, () -> service.approve(requestId));

        verify(requestRepository, never())
                .save(any());
    }
}