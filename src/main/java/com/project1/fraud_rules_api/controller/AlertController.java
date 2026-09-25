package com.project1.fraud_rules_api.controller;

import com.project1.fraud_rules_api.dto.AlertResponse;
import com.project1.fraud_rules_api.service.AlertService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
@RequiredArgsConstructor
@Tag(name = "Alerts", description = "View fraud alerts generated when a transaction triggers one or more fraud rules")
public class AlertController {

    private final AlertService alertService;

    @Operation(summary = "Get all alerts", description = "Returns all fraud alerts, including which rule triggered, the risk score, and the associated transaction")
    @GetMapping
    public ResponseEntity<List<AlertResponse>> getAllAlerts() {
        return ResponseEntity.ok(alertService.getAllAlerts());
    }
}