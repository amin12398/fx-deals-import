package com.example.fxdeal.service;

import com.example.fxdeal.exception.DealValidationException;
import com.example.fxdeal.exception.DuplicateDealException;
import com.example.fxdeal.model.Deal;
import com.example.fxdeal.repository.DealRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DealServiceTest {

    private DealRepository dealRepository;
    private DealService dealService;

    @BeforeEach
    void setup() {
        dealRepository = mock(DealRepository.class);
        dealService = new DealService();
        dealService.dealRepository = dealRepository;
    }

    @Test
    void testValidDealImport() {
        Deal deal = new Deal();
        deal.setDealId("D001");
        deal.setFromCurrency("USD");
        deal.setToCurrency("EUR");
        deal.setTimestamp(LocalDateTime.now());
        deal.setAmount(BigDecimal.valueOf(1000));

        when(dealRepository.existsByDealId("D001")).thenReturn(false);

        Map<String, String> result = dealService.importDeals(List.of(deal));
        assertTrue(result.isEmpty());
        verify(dealRepository, times(1)).save(deal);
    }

    @Test
    void testDuplicateDeal() {
        Deal deal = new Deal();
        deal.setDealId("D001");
        deal.setFromCurrency("USD");
        deal.setToCurrency("EUR");
        deal.setTimestamp(LocalDateTime.now());
        deal.setAmount(BigDecimal.valueOf(1000));

        when(dealRepository.existsByDealId("D001")).thenReturn(true);

        Map<String, String> result = dealService.importDeals(List.of(deal));
        assertEquals("Duplicate deal: D001", result.get("D001"));
    }

    @Test
    void testInvalidDealAmount() {
        Deal deal = new Deal();
        deal.setDealId("D002");
        deal.setFromCurrency("USD");
        deal.setToCurrency("EUR");
        deal.setTimestamp(LocalDateTime.now());
        deal.setAmount(BigDecimal.ZERO);

        Map<String, String> result = dealService.importDeals(List.of(deal));
        assertEquals("Amount must be positive", result.get("D002"));
    }

    @Test
    void testMissingCurrency() {
        Deal deal = new Deal();
        deal.setDealId("D003");
        deal.setFromCurrency(null);
        deal.setToCurrency("EUR");
        deal.setTimestamp(LocalDateTime.now());
        deal.setAmount(BigDecimal.valueOf(100));

        Map<String, String> result = dealService.importDeals(List.of(deal));
        assertEquals("Missing currency", result.get("D003"));
    }
}
