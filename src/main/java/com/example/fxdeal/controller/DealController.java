package com.example.fxdeal.controller;

import com.example.fxdeal.model.Deal;
import com.example.fxdeal.service.DealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/deals")
public class DealController {

    @Autowired
    private DealService dealService;

    @PostMapping("/import")
    public ResponseEntity<?> importDeals(@RequestBody List<Deal> deals) {
        Map<String, String> failedRows = dealService.importDeals(deals);
        int successCount = deals.size() - failedRows.size();
        return ResponseEntity.ok(Map.of(
                "successCount", successCount,
                "failedRows", failedRows
        ));
    }
}
