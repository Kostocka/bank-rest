package com.example.bankcards.service.transfer;

import java.time.YearMonth;
import java.util.UUID;
import com.example.bankcards.dto.request.TransferRequest;
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
public class DefaultTransferCommandService implements TransferCommandService
{
    private final CardRepository cardRepository;

    @Override
    @Transactional
    public void transfer(TransferRequest transferRequest, UUID userId)
    {
        if (transferRequest.fromCardId().equals(transferRequest.toCardId()))
        {
            throw new BusinessException("Cannot transfer to same card");
        }

        Card from = cardRepository
                .findByIdAndOwnerId(transferRequest.fromCardId(), userId)
                .orElseThrow(() -> new NotFoundException("Source card not found"));

        Card to = cardRepository
                .findByIdAndOwnerId(transferRequest.toCardId(), userId)
                .orElseThrow(() -> new NotFoundException("Target card not found"));

        validateCard(from);
        validateCard(to);

        if (from.getBalance().compareTo(transferRequest.amount()) < 0)
        {
            throw new BusinessException("Insufficient balance");
        }

        from.setBalance(from.getBalance().subtract(transferRequest.amount()));
        to.setBalance(to.getBalance().add(transferRequest.amount()));

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