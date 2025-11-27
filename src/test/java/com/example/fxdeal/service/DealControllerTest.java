package com.example.fxdeal.service;

import com.example.fxdeal.controller.DealController;
import com.example.fxdeal.model.Deal;
import com.example.fxdeal.service.DealService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.Mockito.*;

@WebMvcTest(DealController.class)
class DealControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DealService dealService;

    @Test
    void testImportDeals() throws Exception {
        Deal deal = new Deal();
        deal.setDealId("D001");
        deal.setFromCurrency("USD");
        deal.setToCurrency("EUR");
        deal.setTimestamp(LocalDateTime.now());
        deal.setAmount(BigDecimal.valueOf(1000));

        when(dealService.importDeals(anyList())).thenReturn(Map.of());

        String json = "[{\"dealId\":\"D001\",\"fromCurrency\":\"USD\",\"toCurrency\":\"EUR\",\"timestamp\":\"2025-11-25T14:00:00\",\"amount\":1000}]";

        mockMvc.perform(post("/api/deals/import")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.successCount").value(1))
                .andExpect(jsonPath("$.failedRows").isEmpty());

        verify(dealService, times(1)).importDeals(anyList());
    }
}
