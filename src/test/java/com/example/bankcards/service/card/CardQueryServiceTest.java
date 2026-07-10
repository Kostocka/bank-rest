package com.example.bankcards.service.card;

import com.example.bankcards.entity.Card;
import com.example.bankcards.exception.NotFoundException;
import com.example.bankcards.repository.CardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardQueryServiceTest
{
    @Mock
    private CardRepository repository;
    @InjectMocks
    private DefaultCardQueryService service;

    @Test
    void shouldGetCard()
    {
        UUID id = UUID.randomUUID();

        Card card = new Card();
        card.setId(id);

        when(repository.findById(id))
                .thenReturn(Optional.of(card));

        Card result = service.getCard(id);

        assertEquals(card, result);

        verify(repository)
                .findById(id);
    }

    @Test
    void shouldThrowWhenCardNotFound()
    {
        UUID id = UUID.randomUUID();

        when(repository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.getCard(id));
    }

    @Test
    void shouldGetUserCard()
    {
        UUID cardId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Card card = new Card();
        card.setId(cardId);

        when(repository.findByIdAndOwnerId(cardId, userId))
                .thenReturn(Optional.of(card));

        Card result = service.getUserCard(cardId, userId);

        assertEquals(card, result);

        verify(repository)
                .findByIdAndOwnerId(cardId, userId);
    }

    @Test
    void shouldGetUserCards()
    {
        UUID userId = UUID.randomUUID();

        Pageable pageable = PageRequest.of(0, 10);

        Card card = new Card();

        Page<Card> page = new PageImpl<>(List.of(card));

        when(repository.findAll(
                any(Specification.class),
                eq(pageable)
        ))
                .thenReturn(page);

        Page<Card> result =
                service.getUserCards(
                        userId,
                        null,
                        pageable
                );

        assertEquals(1, result.getTotalElements());

        verify(repository)
                .findAll(
                        any(Specification.class),
                        eq(pageable)
                );
    }
}