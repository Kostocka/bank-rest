package com.example.bankcards.service.transfer;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.UUID;
import com.example.bankcards.dto.TransferRequest;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.enums.CardStatus;
import com.example.bankcards.exception.BusinessException;
import com.example.bankcards.exception.NotFoundException;
import com.example.bankcards.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DefaultTransferService implements TransferService
{
    private final CardRepository cardRepository;

    @Override
    @Transactional
    public void transfer(TransferRequest transferRequest)
    {
        BigDecimal amount = transferRequest.amount();
        UUID fromCardId = transferRequest.fromCardId();
        UUID toCardId = transferRequest.toCardId();

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0)
        {
            throw new BusinessException("Amount must be positive");
        }

        if (fromCardId.equals(toCardId))
        {
            throw new BusinessException("Cannot transfer to same card");
        }

        Card from = cardRepository.findById(fromCardId)
                .orElseThrow(() -> new NotFoundException("Source card not found"));

        Card to = cardRepository.findById(toCardId)
                .orElseThrow(() -> new NotFoundException("Target card not found"));

        validateCard(from);
        validateCard(to);

        if (from.getBalance().compareTo(amount) < 0)
        {
            throw new BusinessException("Insufficient balance");
        }

        from.setBalance(from.getBalance().subtract(amount));
        to.setBalance(to.getBalance().add(amount));

        cardRepository.save(from);
        cardRepository.save(to);
    }

    private void validateCard(Card card)
    {
        if (card.getStatus() != CardStatus.ACTIVE)
        {
            throw new BusinessException("Card is not active");
        }

        if (card.getExpirationDate().isBefore(YearMonth.now()))
        {
            throw new BusinessException("Card is expired");
        }
    }
}
