package com.example.bankcards.controller;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;
import com.example.bankcards.dto.response.CardResponse;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.enums.CardStatus;
import com.example.bankcards.security.CurrentUserService;
import com.example.bankcards.security.CustomUserDetailsService;
import com.example.bankcards.security.JwtService;
import com.example.bankcards.service.card.CardFilter;
import com.example.bankcards.service.card.CardQueryService;
import com.example.bankcards.util.mapper.CardMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CardController.class)
@AutoConfigureMockMvc(addFilters = false)
class CardControllerTest
{
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CardQueryService queryService;

    @MockitoBean
    private CurrentUserService currentUser;

    @MockitoBean
    private CardMapper mapper;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void shouldGetUserCard() throws Exception
    {
        UUID cardId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Card card = new Card();

        CardResponse response = new CardResponse(
                cardId,
                "**** **** **** 1234",
                YearMonth.now().plusYears(2),
                CardStatus.ACTIVE,
                BigDecimal.ZERO,
                userId
        );

        when(currentUser.getCurrentUserId())
                .thenReturn(userId);
        when(queryService.getUserCard(cardId, userId))
                .thenReturn(card);
        when(mapper.toResponse(card))
                .thenReturn(response);

        mockMvc.perform(get("/cards/{id}", cardId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id")
                        .value(cardId.toString()))
                .andExpect(jsonPath("$.status")
                        .value("ACTIVE"));
    }

    @Test
    void shouldGetUserCards() throws Exception
    {
        UUID userId = UUID.randomUUID();

        when(currentUser.getCurrentUserId())
                .thenReturn(userId);
        when(queryService.getUserCards(
                eq(userId),
                any(CardFilter.class),
                any()
        ))
                .thenReturn(new PageImpl<>(List.of()));

        mockMvc.perform(get("/cards"))
                .andExpect(status().isOk());
    }
}