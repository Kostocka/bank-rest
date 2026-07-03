package com.example.bankcards.util;

public interface CardEncryptor
{
    String encrypt(String originCardNumber);

    String decrypt(String encryptedCardNumber);
}