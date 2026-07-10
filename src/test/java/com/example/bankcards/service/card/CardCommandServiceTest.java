package com.example.bankcards.service.card;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Optional;
import java.util.UUID;
import com.example.bankcards.dto.request.CreateCardRequest;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.entity.enums.CardStatus;
import com.example.bankcards.exception.BusinessException;
import com.example.bankcards.exception.NotFoundException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.util.CardEncryptor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardCommandServiceTest
{
    @Mock
    private CardRepository cardRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CardEncryptor encryptor;
    @InjectMocks
    private DefaultCardCommandService service;

    @Test
    void shouldCreateCard()
    {
        UUID ownerId = UUID.randomUUID();

        CreateCardRequest request =
                new CreateCardRequest(
                        "1234567890123456",
                        YearMonth.now().plusYears(2),
                        BigDecimal.ZERO,
                        ownerId
                );

        User user = new User();
        user.setId(ownerId);

        when(encryptor.encrypt(request.cardNumber()))
                .thenReturn("encrypted");
        when(cardRepository.existsByCardNumber("encrypted"))
                .thenReturn(false);
        when(userRepository.findById(ownerId))
                .thenReturn(Optional.of(user));
        when(cardRepository.save(any(Card.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Card result = service.create(request);

        assertNotNull(result);
        assertEquals(user, result.getOwner());
        assertEquals("encrypted", result.getCardNumber());
        assertEquals(CardStatus.ACTIVE, result.getStatus());
        assertEquals(BigDecimal.ZERO, result.getBalance());
        assertEquals(request.expirationDate(), result.getExpirationDate());

        verify(cardRepository).save(any(Card.class));
    }

    @Test
    void shouldThrowWhenCardAlreadyExists()
    {
        CreateCardRequest request =
                new CreateCardRequest(
                        "1234567890123456",
                        YearMonth.now().plusYears(2),
                        BigDecimal.ZERO,
                        UUID.randomUUID()
                );

        when(encryptor.encrypt(any()))
                .thenReturn("encrypted");
        when(cardRepository.existsByCardNumber("encrypted"))
                .thenReturn(true);

        assertThrows(BusinessException.class, () -> service.create(request));
    }

    @Test
    void shouldDeleteCard()
    {
        UUID cardId = UUID.randomUUID();

        Card card = new Card();
        card.setId(cardId);

        when(cardRepository.findById(cardId))
                .thenReturn(Optional.of(card));

        service.delete(cardId);

        verify(cardRepository).delete(card);
    }

    @Test
    void shouldThrowWhenDeleteCardNotFound()
    {
        UUID cardId = UUID.randomUUID();

        when(cardRepository.findById(cardId))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.delete(cardId));

        verify(cardRepository, never()).delete((Card) any());
    }

    @Test
    void shouldActivateCard()
    {
        UUID cardId = UUID.randomUUID();

        Card card = new Card();
        card.setId(cardId);
        card.setStatus(CardStatus.BLOCKED);

        when(cardRepository.findById(cardId))
                .thenReturn(Optional.of(card));
        when(cardRepository.save(card))
                .thenReturn(card);

        Card result = service.activate(cardId);

        assertEquals(CardStatus.ACTIVE, result.getStatus());

        verify(cardRepository)
                .save(card);
    }

    @Test
    void shouldNotActivateExpiredCard()
    {
        UUID cardId = UUID.randomUUID();

        Card card = new Card();

        card.setId(cardId);
        card.setStatus(CardStatus.EXPIRED);

        when(cardRepository.findById(cardId))
                .thenReturn(Optional.of(card));

        assertThrows(BusinessException.class, () -> service.activate(cardId));

        verify(cardRepository, never())
                .save(any());
    }

    @Test
    void shouldBlockCard()
    {
        UUID cardId = UUID.randomUUID();

        Card card = new Card();
        card.setId(cardId);
        card.setStatus(CardStatus.ACTIVE);

        when(cardRepository.findById(cardId))
                .thenReturn(Optional.of(card));
        when(cardRepository.save(card))
                .thenReturn(card);

        Card result = service.block(cardId);

        assertEquals(CardStatus.BLOCKED, result.getStatus());

        verify(cardRepository)
                .save(card);
    }

    @Test
    void shouldNotBlockExpiredCard()
    {
        UUID cardId = UUID.randomUUID();

        Card card = new Card();
        card.setId(cardId);
        card.setStatus(CardStatus.EXPIRED);

        when(cardRepository.findById(cardId))
                .thenReturn(Optional.of(card));

        assertThrows(BusinessException.class, () -> service.block(cardId));

        verify(cardRepository, never())
                .save(any());
    }
}