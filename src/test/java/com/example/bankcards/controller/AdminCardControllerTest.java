package com.example.bankcards.controller;

import java.time.YearMonth;
import java.util.List;
import java.util.UUID;
import com.example.bankcards.dto.request.CreateCardRequest;
import com.example.bankcards.entity.Card;
import com.example.bankcards.security.CustomUserDetailsService;
import com.example.bankcards.security.JwtService;
import com.example.bankcards.service.card.CardCommandService;
import com.example.bankcards.service.card.CardQueryService;
import com.example.bankcards.util.mapper.CardMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminCardController.class)
@AutoConfigureMockMvc(addFilters = false)
class AdminCardControllerTest
{
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CardCommandService commandService;

    @MockitoBean
    private CardQueryService queryService;

    @MockitoBean
    private CardMapper mapper;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void shouldCreateCard() throws Exception
    {
        CreateCardRequest request =
                new CreateCardRequest(
                        "1234567890123456",
                        YearMonth.now().plusYears(2),
                        null,
                        UUID.randomUUID()
                );

        Card card = new Card();

        when(commandService.create(any()))
                .thenReturn(card);

        mockMvc.perform(post("/admin/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetCard() throws Exception
    {
        UUID id = UUID.randomUUID();

        Card card = new Card();

        when(queryService.getCard(id))
                .thenReturn(card);

        mockMvc.perform(get("/admin/cards/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetAllCards() throws Exception
    {
        when(queryService.getCards(any(), any()))
                .thenReturn(new PageImpl<>(List.of()));

        mockMvc.perform(get("/admin/cards"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteCard() throws Exception
    {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/admin/cards/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    void shouldActivateCard() throws Exception
    {
        UUID id = UUID.randomUUID();

        when(commandService.activate(id))
                .thenReturn(new Card());

        mockMvc.perform(post("/admin/cards/{id}/activate", id))
                .andExpect(status().isOk());
    }

    @Test
    void shouldBlockCard() throws Exception
    {
        UUID id = UUID.randomUUID();

        when(commandService.block(id))
                .thenReturn(new Card());

        mockMvc.perform(post("/admin/cards/{id}/block", id))
                .andExpect(status().isOk());
    }
}