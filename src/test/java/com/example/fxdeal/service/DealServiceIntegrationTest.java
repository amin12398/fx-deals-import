package com.example.fxdeal.service;

import com.example.fxdeal.model.Deal;
import com.example.fxdeal.repository.DealRepository;
import com.example.fxdeal.service.DealService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class DealServiceIntegrationTest {

    @Autowired
    private DealRepository dealRepository;

    @Autowired
    private DealService dealService;

    private Deal validDeal;
    private Deal invalidDeal;
    private Deal duplicateDeal;

    @BeforeEach
    void setup() {
        dealRepository.deleteAll();

        validDeal = new Deal();
        validDeal.setDealId("D001");
        validDeal.setFromCurrency("USD");
        validDeal.setToCurrency("EUR");
        validDeal.setTimestamp(LocalDateTime.now());
        validDeal.setAmount(BigDecimal.valueOf(1000));

        invalidDeal = new Deal();
        invalidDeal.setDealId("");
        invalidDeal.setFromCurrency("USD");
        invalidDeal.setToCurrency("EUR");
        invalidDeal.setTimestamp(LocalDateTime.now());
        invalidDeal.setAmount(BigDecimal.valueOf(1000));

        duplicateDeal = new Deal();
        duplicateDeal.setDealId("D001");
        duplicateDeal.setFromCurrency("USD");
        duplicateDeal.setToCurrency("EUR");
        duplicateDeal.setTimestamp(LocalDateTime.now());
        duplicateDeal.setAmount(BigDecimal.valueOf(2000));
    }

    @Test
    void testImportDeals_ValidInvalidDuplicate() {
        List<Deal> deals = List.of(validDeal, invalidDeal, duplicateDeal);

        Map<String, String> failedRows = dealService.importDeals(deals);

        assertEquals(2, failedRows.size());
        assertTrue(failedRows.containsKey(invalidDeal.getDealId()));
        assertTrue(failedRows.containsKey(duplicateDeal.getDealId()));

        assertEquals(1, dealRepository.count());
        assertEquals(BigDecimal.valueOf(1000), dealRepository.findByDealId("D001").get().getAmount());
    }

    @Test
    void testMultipleValidDeals_NoRollback() {
        Deal deal2 = new Deal();
        deal2.setDealId("D002");
        deal2.setFromCurrency("GBP");
        deal2.setToCurrency("USD");
        deal2.setTimestamp(LocalDateTime.now());
        deal2.setAmount(BigDecimal.valueOf(500));

        Map<String, String> failedRows = dealService.importDeals(List.of(validDeal, deal2));

        assertTrue(failedRows.isEmpty());
        assertEquals(2, dealRepository.count());
    }
}
