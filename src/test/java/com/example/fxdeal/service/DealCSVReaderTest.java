package com.example.fxdeal.service;

import com.example.fxdeal.model.Deal;
import com.example.fxdeal.util.DealCSVReader;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DealCSVReaderTest {

    private final DealCSVReader reader = new DealCSVReader();

    @Test
    void testReadDeals() {
        List<Deal> deals = reader.readDeals();

        assertFalse(deals.isEmpty());
        assertEquals("D001", deals.get(0).getDealId());
        assertNotNull(deals.get(0).getTimestamp());
    }
}
