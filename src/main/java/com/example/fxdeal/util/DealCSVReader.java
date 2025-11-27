package com.example.fxdeal.util;

import com.example.fxdeal.model.Deal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class DealCSVReader {

    private static final Logger log = LoggerFactory.getLogger(DealCSVReader.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public List<Deal> readDeals() {
        List<Deal> deals = new ArrayList<>();
        log.info("Starting to read sample_deals.csv");

        try {
            ClassPathResource resource = new ClassPathResource("sample_deals.csv");

            try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
                String line;
                boolean firstLine = true;

                while ((line = br.readLine()) != null) {
                    if (firstLine) { firstLine = false; continue; }

                    log.debug("Reading row: {}", line);

                    String[] columns = line.split(",");
                    if (columns.length < 5) {
                        log.warn("Skipping row due to insufficient columns: {}", line);
                        continue;
                    }

                    Deal deal = new Deal();
                    deal.setDealId(columns[0].trim());
                    deal.setFromCurrency(columns[1].trim());
                    deal.setToCurrency(columns[2].trim());

                    try {
                        deal.setTimestamp(LocalDateTime.parse(columns[3].trim(), formatter));
                    } catch (Exception e) {
                        log.warn("Invalid timestamp for dealId {}: {}", columns[0], columns[3]);
                        deal.setTimestamp(null);
                    }

                    try {
                        deal.setAmount(new BigDecimal(columns[4].trim()));
                    } catch (Exception e) {
                        log.warn("Invalid amount for dealId {}: {}", columns[0], columns[4]);
                        deal.setAmount(null);
                    }

                    deals.add(deal);
                }
            }
        } catch (Exception e) {
            log.error("Failed to read CSV file", e);
        }

        log.info("Finished reading CSV. Total rows parsed: {}", deals.size());
        return deals;
    }
}
