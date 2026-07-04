package com.example.bankcards.repository.spec;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.enums.CardStatus;
import org.springframework.data.jpa.domain.Specification;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.UUID;

public class CardSpecification
{
    public static Specification<Card> hasStatus(CardStatus status)
    {
        return (root, query, cb) ->
                status == null ? null : cb.equal(root.get("status"), status);
    }

    public static Specification<Card> hasExpiration(YearMonth date)
    {
        return (root, query, cb) ->
                date == null ? null : cb.equal(root.get("expirationDate"), date);
    }

    public static Specification<Card> balanceGte(BigDecimal min)
    {
        return (root, query, cb) ->
                min == null ? null : cb.greaterThanOrEqualTo(root.get("balance"), min);
    }

    public static Specification<Card> balanceLte(BigDecimal max)
    {
        return (root, query, cb) ->
                max == null ? null : cb.lessThanOrEqualTo(root.get("balance"), max);
    }

    public static Specification<Card> hasOwner(UUID ownerId)
    {
        return (root, query, cb) ->
                ownerId == null ? null : cb.equal(root.get("owner").get("id"), ownerId);
    }
}