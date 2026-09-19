package com.project1.fraud_rules_api.dto;

import com.project1.fraud_rules_api.entity.AlertStatus;
import com.project1.fraud_rules_api.entity.RuleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class AlertResponse {
    private Long id;
    private Long transactionId;
    private RuleType ruleType;
    private Integer riskScore;
    private AlertStatus status;
    private LocalDateTime createdAt;
}