package com.example.bankcards.util;

import org.springframework.stereotype.Component;

@Component
public class DefaultCardNumberMasker implements CardNumberMasker
{
    @Override
    public String mask(String cardNumber)
    {
        if (cardNumber == null || cardNumber.length() < 4)
        {
            throw new IllegalArgumentException("Invalid card number");
        }

        String last4 = cardNumber.substring(cardNumber.length() - 4);

        return "**** **** **** " + last4;
    }
}
