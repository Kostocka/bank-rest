package com.example.bankcards.service.card;

import java.util.UUID;
import com.example.bankcards.entity.Card;
import com.example.bankcards.exception.NotFoundException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.spec.CardSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultCardQueryService implements CardQueryService
{
    private final CardRepository repository;

    @Override
    public Card getCard(UUID id)
    {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Card not found"));
    }

    @Override
    public Page<Card> getCards(CardFilter filter, Pageable pageable)
    {
        Specification<Card> spec = CardFilterMapper.toSpec(filter);

        return repository.findAll(spec, pageable);
    }

    @Override
    public Card getUserCard(UUID cardId, UUID userId)
    {
        return repository.findByIdAndOwnerId(cardId, userId)
                .orElseThrow(() -> new NotFoundException("Card not found"));
    }

    @Override
    public Page<Card> getUserCards(UUID userId, CardFilter filter, Pageable pageable)
    {
        Specification<Card> spec = CardSpecification.hasOwner(userId);

        spec = spec.and(CardFilterMapper.toSpec(filter));

        return repository.findAll(spec, pageable);
    }
}