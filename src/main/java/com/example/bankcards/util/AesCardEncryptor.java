package com.example.bankcards.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component
public class AesCardEncryptor implements CardEncryptor
{
    private static final String ALGORITHM = "AES";

    private final SecretKeySpec keySpec;

    public AesCardEncryptor(@Value("${encryption.secret-key}") String secretKey)
    {
        this.keySpec = new SecretKeySpec(secretKey.getBytes(), ALGORITHM);
    }

    @Override
    public String encrypt(String cardNumber)
    {
        try
        {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);

            byte[] encrypted = cipher.doFinal(cardNumber.getBytes());

            return Base64.getEncoder().encodeToString(encrypted);

        } catch (Exception e)
        {
            throw new RuntimeException("Failed to encrypt card number", e);
        }
    }

    @Override
    public String decrypt(String encryptedCardNumber)
    {
        try
        {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, keySpec);

            byte[] decoded = Base64.getDecoder().decode(encryptedCardNumber);

            return new String(cipher.doFinal(decoded));

        } catch (Exception e)
        {
            throw new RuntimeException("Failed to decrypt card number", e);
        }
    }
}