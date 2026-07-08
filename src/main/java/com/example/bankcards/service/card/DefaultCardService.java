package com.example.bankcards.service.card;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.UUID;
import com.example.bankcards.dto.request.CreateCardRequest;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.entity.enums.CardStatus;
import com.example.bankcards.exception.BusinessException;
import com.example.bankcards.exception.NotFoundException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.repository.spec.CardSpecification;
import com.example.bankcards.security.CurrentUserService;
import com.example.bankcards.service.access.AccessService;
import com.example.bankcards.util.CardEncryptor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultCardService implements CardService
{
    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final CardEncryptor cardEncryptor;
    private final CurrentUserService currentUserService;
    private final AccessService accessService;

    @Override
    public Card createCard(CreateCardRequest req)
    {
        accessService.requireAdmin();

        String encrypted = cardEncryptor.encrypt(req.cardNumber());
        if (cardRepository.existsByCardNumber(encrypted))
        {
            throw new BusinessException("Card already exists");
        }

        if (req.expirationDate().isBefore(YearMonth.now()))
        {
            throw new BusinessException("Cannot create expired card");
        }

        User owner = userRepository.findById(req.ownerId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        Card card = new Card();

        card.setOwner(owner);
        card.setExpirationDate(req.expirationDate());

        card.setCardNumber(encrypted);

        card.setStatus(CardStatus.ACTIVE);

        card.setBalance(req.balance() != null ? req.balance() : BigDecimal.ZERO);

        return cardRepository.save(card);
    }

    @Override
    public Card activateCard(UUID cardId)
    {
        accessService.requireAdmin();
        Card card = requireCard(cardId);

        if (card.getStatus() == CardStatus.EXPIRED)
        {
            throw new BusinessException("Cannot activate expired card");
        }

        card.setStatus(CardStatus.ACTIVE);

        return cardRepository.save(card);
    }

    @Override
    public Card blockCard(UUID cardId)
    {
        accessService.requireAdmin();
        Card card = requireCard(cardId);

        if (card.getStatus() == CardStatus.EXPIRED)
        {
            throw new BusinessException("Card already expired");
        }

        card.setStatus(CardStatus.BLOCKED);
        return cardRepository.save(card);
    }

    @Override
    public void deleteCard(UUID cardId)
    {
        accessService.requireAdmin();
        Card card = requireCard(cardId);

        cardRepository.delete(card);
    }

    @Override
    public Card getCard(UUID cardId)
    {
        return requireOwnedCard(cardId);
    }

    @Override
    public Page<Card> getCards(CardFilter filter, Pageable pageable)
    {
        var spec = CardFilterMapper.toSpec(filter);

        if (!currentUserService.isAdmin())
        {
            spec = spec.and(CardSpecification.hasOwner(currentUserService.getCurrentUserId()));
        }

        return cardRepository.findAll(spec, pageable);
    }

    @Override
    public BigDecimal getBalance(UUID cardId)
    {
        return requireOwnedCard(cardId).getBalance();
    }

    private Card requireCard(UUID cardId)
    {
        return cardRepository.findById(cardId)
                .orElseThrow(() -> new NotFoundException("Card not found"));
    }

    private Card requireOwnedCard(UUID cardId)
    {
        Card card = requireCard(cardId);

        if (!currentUserService.isAdmin()
                && !card.getOwner().getId().equals(currentUserService.getCurrentUserId()))
        {
            throw new BusinessException("Access denied");
        }

        return card;
    }
}
