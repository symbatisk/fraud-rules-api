package com.project1.fraud_rules_api.service;

import com.project1.fraud_rules_api.entity.*;
import com.project1.fraud_rules_api.repository.AlertRepository;
import com.project1.fraud_rules_api.repository.AuditLogRepository;
import com.project1.fraud_rules_api.repository.FraudRuleRepository;
import com.project1.fraud_rules_api.service.rules.FraudRuleChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FraudDetectionService {

    private final FraudRuleRepository fraudRuleRepository;
    private final AlertRepository alertRepository;
    private final AuditLogRepository auditLogRepository;
    private final List<FraudRuleChecker> ruleCheckers;

    private Map<RuleType, FraudRuleChecker> checkersByType;


    private Map<RuleType, FraudRuleChecker> getCheckersByType() {
        if (checkersByType == null) {
            checkersByType = ruleCheckers.stream()
                    .collect(Collectors.toMap(FraudRuleChecker::getSupportedType, c -> c));
        }
        return checkersByType;
    }

    public void evaluateTransaction(Transaction transaction) {
        List<FraudRule> activeRules = fraudRuleRepository.findByIsActiveTrue();

        for (FraudRule rule : activeRules) {
            FraudRuleChecker checker = getCheckersByType().get(rule.getRuleType());

            if (checker == null) {
                continue;
            }

            int riskScore = checker.check(transaction, rule);

            if (riskScore > 0) {
                createAlert(transaction, rule, riskScore);
            }
        }

        logAudit("TRANSACTION", transaction.getId(), "CREATED",
                "Transaction created for user " + transaction.getUser().getId());
    }

    private void createAlert(Transaction transaction, FraudRule rule, int riskScore) {
        Alert alert = new Alert();
        alert.setTransaction(transaction);
        alert.setRule(rule);
        alert.setRiskScore(riskScore);
        alert.setStatus(AlertStatus.OPEN);

        alertRepository.save(alert);

        transaction.setStatus(TransactionStatus.FLAGGED);

        logAudit("ALERT", alert.getId(), "FLAGGED",
                "Rule " + rule.getRuleType() + " triggered with risk score " + riskScore);
    }

    private void logAudit(String entityType, Long entityId, String action, String details) {
        AuditLog log = new AuditLog();
        log.setEntityType(entityType);
        log.setEntityId(entityId);
        log.setAction(action);
        log.setDetails(details);

        auditLogRepository.save(log);
    }
}