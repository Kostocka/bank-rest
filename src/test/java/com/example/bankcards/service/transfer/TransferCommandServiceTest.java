package com.example.bankcards.service.transfer;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Optional;
import java.util.UUID;
import com.example.bankcards.dto.request.TransferRequest;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.enums.CardStatus;
import com.example.bankcards.exception.BusinessException;
import com.example.bankcards.exception.NotFoundException;
import com.example.bankcards.repository.CardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransferCommandServiceTest
{
    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private DefaultTransferCommandService service;

    private UUID userId;
    private UUID fromId;
    private UUID toId;
    private Card from;
    private Card to;

    @BeforeEach
    void setUp()
    {
        MockitoAnnotations.openMocks(this);

        userId = UUID.randomUUID();
        fromId = UUID.randomUUID();
        toId = UUID.randomUUID();

        from = new Card();
        from.setId(fromId);
        from.setBalance(new BigDecimal("1000"));
        from.setStatus(CardStatus.ACTIVE);
        from.setExpirationDate(
                YearMonth.now().plusYears(1)
        );

        to = new Card();
        to.setId(toId);
        to.setBalance(new BigDecimal("500"));
        to.setStatus(CardStatus.ACTIVE);
        to.setExpirationDate(
                YearMonth.now().plusYears(1)
        );
    }

    @Test
    void shouldTransferMoney()
    {
        TransferRequest request =
                new TransferRequest(
                        fromId,
                        toId,
                        new BigDecimal("300")
                );

        when(cardRepository.findByIdAndOwnerId(fromId, userId))
                .thenReturn(Optional.of(from));
        when(cardRepository.findByIdAndOwnerId(toId, userId))
                .thenReturn(Optional.of(to));

        service.transfer(request, userId);

        assertEquals(new BigDecimal("700"), from.getBalance());
        assertEquals(new BigDecimal("800"), to.getBalance());

        verify(cardRepository)
                .save(from);
        verify(cardRepository)
                .save(to);
    }

    @Test
    void shouldThrowWhenTransferToSameCard()
    {
        TransferRequest request =
                new TransferRequest(
                        fromId,
                        fromId,
                        new BigDecimal("100")
                );

        assertThrows(BusinessException.class, () -> service.transfer(request, userId));

        verifyNoInteractions(cardRepository);
    }

    @Test
    void shouldThrowWhenSourceCardNotFound()
    {
        TransferRequest request =
                new TransferRequest(
                        fromId,
                        toId,
                        new BigDecimal("100")
                );

        when(cardRepository.findByIdAndOwnerId(fromId, userId))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.transfer(request, userId));
    }

    @Test
    void shouldThrowWhenTargetCardNotFound()
    {
        TransferRequest request =
                new TransferRequest(
                        fromId,
                        toId,
                        new BigDecimal("100")
                );

        when(cardRepository.findByIdAndOwnerId(fromId, userId))
                .thenReturn(Optional.of(from));
        when(cardRepository.findByIdAndOwnerId(toId, userId))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.transfer(request, userId));
    }

    @Test
    void shouldThrowWhenInsufficientBalance()
    {
        TransferRequest request =
                new TransferRequest(
                        fromId,
                        toId,
                        new BigDecimal("2000")
                );

        when(cardRepository.findByIdAndOwnerId(fromId, userId))
                .thenReturn(Optional.of(from));
        when(cardRepository.findByIdAndOwnerId(toId, userId))
                .thenReturn(Optional.of(to));

        assertThrows(BusinessException.class, () -> service.transfer(request, userId));
        assertEquals(new BigDecimal("1000"), from.getBalance());
    }

    @Test
    void shouldThrowWhenCardBlocked()
    {
        from.setStatus(CardStatus.BLOCKED);

        TransferRequest request =
                new TransferRequest(
                        fromId,
                        toId,
                        new BigDecimal("100")
                );

        when(cardRepository.findByIdAndOwnerId(fromId, userId))
                .thenReturn(Optional.of(from));
        when(cardRepository.findByIdAndOwnerId(toId, userId))
                .thenReturn(Optional.of(to));

        assertThrows(BusinessException.class, () -> service.transfer(request, userId));
    }

    @Test
    void shouldThrowWhenCardExpired()
    {
        from.setExpirationDate(
                YearMonth.now().minusMonths(1)
        );

        TransferRequest request =
                new TransferRequest(
                        fromId,
                        toId,
                        new BigDecimal("100")
                );

        when(cardRepository.findByIdAndOwnerId(fromId, userId))
                .thenReturn(Optional.of(from));
        when(cardRepository.findByIdAndOwnerId(toId, userId))
                .thenReturn(Optional.of(to));

        assertThrows(BusinessException.class, () -> service.transfer(request, userId));
    }
}