package com.example.bankcards.service.card;

import java.math.BigDecimal;
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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultCardCommandService implements CardCommandService
{
    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final CardEncryptor encryptor;

    @Override
    public Card create(CreateCardRequest req)
    {
        String encrypted = encryptor.encrypt(req.cardNumber());

        if(cardRepository.existsByCardNumber(encrypted))
        {
            throw new BusinessException("Card already exists");
        }

        User owner = userRepository.findById(req.ownerId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        Card card = new Card();

        card.setOwner(owner);
        card.setCardNumber(encrypted);
        card.setExpirationDate(req.expirationDate());
        card.setStatus(CardStatus.ACTIVE);
        card.setBalance(req.balance() == null ? BigDecimal.ZERO : req.balance());

        return cardRepository.save(card);
    }

    @Override
    public void delete(UUID cardId)
    {
        Card card = requireCard(cardId);

        cardRepository.delete(card);
    }

    @Override
    public Card activate(UUID id)
    {
        Card card = requireCard(id);

        if (card.getStatus() == CardStatus.EXPIRED)
        {
            throw new BusinessException("Card already expired");
        }

        card.setStatus(CardStatus.ACTIVE);

        return cardRepository.save(card);
    }

    @Override
    public Card block(UUID cardId)
    {
        Card card = requireCard(cardId);

        if (card.getStatus() == CardStatus.EXPIRED)
        {
            throw new BusinessException("Card already expired");
        }

        card.setStatus(CardStatus.BLOCKED);
        return cardRepository.save(card);
    }


    private Card requireCard(UUID id)
    {
        return cardRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Card not found"));
    }
}