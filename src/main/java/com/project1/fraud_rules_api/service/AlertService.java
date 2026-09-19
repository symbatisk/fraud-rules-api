package com.project1.fraud_rules_api.service;

import com.project1.fraud_rules_api.dto.AlertResponse;
import com.project1.fraud_rules_api.entity.Alert;
import com.project1.fraud_rules_api.repository.AlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlertService {

    private final AlertRepository alertRepository;

    public List<AlertResponse> getAllAlerts() {
        return alertRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private AlertResponse toResponse(Alert alert) {
        return new AlertResponse(
                alert.getId(),
                alert.getTransaction().getId(),
                alert.getRule().getRuleType(),
                alert.getRiskScore(),
                alert.getStatus(),
                alert.getCreatedAt()
        );
    }
}