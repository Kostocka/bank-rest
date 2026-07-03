package com.example.bankcards.service.card;

import com.example.bankcards.entity.Card;
import com.example.bankcards.repository.spec.CardSpecification;
import org.springframework.data.jpa.domain.Specification;

public class CardFilterMapper
{
    public static Specification<Card> toSpec(CardFilter filter)
    {
        if (filter == null)
        {
            return Specification.allOf();
        }

        Specification<Card> spec = Specification.allOf();

        if (filter.status() != null) {
            spec = spec.and(CardSpecification.hasStatus(filter.status()));
        }

        if (filter.expirationDate() != null) {
            spec = spec.and(CardSpecification.hasExpiration(filter.expirationDate()));
        }

        if (filter.minBalance() != null) {
            spec = spec.and(CardSpecification.balanceGte(filter.minBalance()));
        }

        if (filter.maxBalance() != null) {
            spec = spec.and(CardSpecification.balanceLte(filter.maxBalance()));
        }

        return spec;
    }
}