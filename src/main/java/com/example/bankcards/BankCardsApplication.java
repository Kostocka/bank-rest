package com.example.bankcards;

import com.example.bankcards.config.AdminProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AdminProperties.class)
public class BankCardsApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(BankCardsApplication.class, args);
    }
}
