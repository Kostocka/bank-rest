package com.example.bankcards.service.card;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.UUID;
import com.example.bankcards.dto.CreateCardRequest;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.entity.enums.CardStatus;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.repository.spec.CardSpecification;
import com.example.bankcards.util.CardEncryptor;
import com.example.bankcards.util.CardNumberMasker;
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
    private final CardNumberMasker cardNumberMasker;

    @Override
    public Card createCard(CreateCardRequest req)
    {
        if (req.cardNumber() == null || req.cardNumber().isBlank())
        {
            throw new RuntimeException("Card number is required");
        }

        if (req.expirationDate() == null)
        {
            throw new RuntimeException("Expiration date is required");
        }

        if (req.expirationDate().isBefore(YearMonth.now()))
        {
            throw new RuntimeException("Cannot create expired card");
        }

        User owner = userRepository.findById(req.ownerId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Card card = new Card();

        card.setOwner(owner);
        card.setExpirationDate(req.expirationDate());

        card.setCardNumber(cardEncryptor.encrypt(req.cardNumber()));

        card.setStatus(CardStatus.ACTIVE);

        card.setBalance(req.balance() != null ? req.balance() : BigDecimal.ZERO);

        return cardRepository.save(card);
    }

    @Override
    public Card activateCard(UUID cardId)
    {
        Card card = getCard(cardId);

        if (card.getStatus() == CardStatus.EXPIRED)
        {
            throw new RuntimeException("Cannot activate expired card");
        }

        card.setStatus(CardStatus.ACTIVE);
        return cardRepository.save(card);
    }

    @Override
    public Card blockCard(UUID cardId)
    {
        Card card = getCard(cardId);

        if (card.getStatus() == CardStatus.EXPIRED)
        {
            throw new RuntimeException("Card already expired");
        }

        card.setStatus(CardStatus.BLOCKED);
        return cardRepository.save(card);
    }

    @Override
    public void deleteCard(UUID cardId)
    {
        cardRepository.deleteById(cardId);
    }

    @Override
    public Card getCard(UUID cardId)
    {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found"));

        card.setCardNumber(cardNumberMasker.mask(cardEncryptor.decrypt(card.getCardNumber())));

        return card;
    }

    @Override
    public Page<Card> getCards(CardFilter filter, Pageable pageable)
    {
        var spec = CardFilterMapper.toSpec(filter);
        return cardRepository.findAll(spec, pageable)
                .map(card ->
                {
                    card.setCardNumber(cardNumberMasker.mask(cardEncryptor.decrypt(card.getCardNumber())));
                    return card;
                });
    }

    @Override
    public Page<Card> getUserCards(UUID userId, CardFilter filter, Pageable pageable)
    {
        var spec = CardFilterMapper.toSpec(filter).and(CardSpecification.hasOwner(userId));
        return cardRepository.findAll(spec, pageable);
    }

    @Override
    public BigDecimal getBalance(UUID cardId)
    {
        return getCard(cardId).getBalance();
    }
}
