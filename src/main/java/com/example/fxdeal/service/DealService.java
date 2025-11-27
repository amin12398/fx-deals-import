package com.example.fxdeal.service;

import com.example.fxdeal.exception.DealValidationException;
import com.example.fxdeal.exception.DuplicateDealException;
import com.example.fxdeal.model.Deal;
import com.example.fxdeal.repository.DealRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DealService {

    private static final Logger log = LoggerFactory.getLogger(DealService.class);

    @Autowired
    public DealRepository dealRepository;

    public Map<String, String> importDeals(List<Deal> deals) {
        log.info("Starting import of {} deals", deals.size());

        Map<String, String> failedRows = new HashMap<>();

        for (Deal deal : deals) {
            try {
                validateDeal(deal);

                if (!dealRepository.existsByDealId(deal.getDealId())) {
                    dealRepository.save(deal);
                    log.info("Saved deal {}", deal.getDealId());
                } else {
                    log.warn("Duplicate deal detected: {}", deal.getDealId());
                    throw new DuplicateDealException(deal.getDealId());
                }

            } catch (DealValidationException | DuplicateDealException e) {
                log.error("Failed to import deal {}: {}", deal.getDealId(), e.getMessage());
                failedRows.put(deal.getDealId(), e.getMessage());
            } catch (Exception e) {
                log.error("Unexpected error for deal {}: {}", deal.getDealId(), e.getMessage(), e);
                failedRows.put(deal.getDealId(), "Unexpected error: " + e.getMessage());
            }
        }

        log.info("Import completed. {} failed, {} succeeded",
                failedRows.size(),
                deals.size() - failedRows.size());

        return failedRows;
    }

    private void validateDeal(Deal deal) {
        if (deal.getDealId() == null || deal.getDealId().isEmpty()) {
            throw new DealValidationException("Missing dealId");
        }
        if (deal.getFromCurrency() == null || deal.getToCurrency() == null) {
            throw new DealValidationException("Missing currency");
        }
        if (deal.getTimestamp() == null) {
            throw new DealValidationException("Invalid or missing timestamp");
        }
        if (deal.getAmount() == null) {
            throw new DealValidationException("Missing amount");
        }
        if (deal.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new DealValidationException("Amount must be positive");
        }
    }
}
