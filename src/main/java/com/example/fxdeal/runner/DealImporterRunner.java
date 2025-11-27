package com.example.fxdeal.runner;

import com.example.fxdeal.model.Deal;
import com.example.fxdeal.service.DealService;
import com.example.fxdeal.util.DealCSVReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class DealImporterRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DealImporterRunner.class);

    private final DealCSVReader dealCSVReader;
    private final DealService dealService;

    public DealImporterRunner(DealCSVReader dealCSVReader, DealService dealService) {
        this.dealCSVReader = dealCSVReader;
        this.dealService = dealService;
    }

    @Override
    public void run(String... args) {
        log.info("Starting Deal Import Runner...");

        try {
            List<Deal> deals = dealCSVReader.readDeals();
            Map<String, String> failedRows = dealService.importDeals(deals);

            if (failedRows.isEmpty()) {
                log.info("All deals imported successfully!");
            } else {
                log.warn("Import completed with failures:");
                failedRows.forEach((id, reason) ->
                        log.warn("Deal {} failed: {}", id, reason));
            }

        } catch (Exception e) {
            log.error("Unexpected error in DealImporterRunner", e);
        }

        log.info("Deal Import Runner finished.");
    }

}
