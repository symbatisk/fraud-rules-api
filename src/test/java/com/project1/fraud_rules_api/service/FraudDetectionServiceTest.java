package com.project1.fraud_rules_api.service;

import com.project1.fraud_rules_api.entity.*;
import com.project1.fraud_rules_api.repository.AlertRepository;
import com.project1.fraud_rules_api.repository.AuditLogRepository;
import com.project1.fraud_rules_api.repository.FraudRuleRepository;
import com.project1.fraud_rules_api.service.rules.FraudRuleChecker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FraudDetectionServiceTest {

    @Mock
    private FraudRuleRepository fraudRuleRepository;

    @Mock
    private AlertRepository alertRepository;

    @Mock
    private AuditLogRepository auditLogRepository;

    @Mock
    private FraudRuleChecker amountThresholdChecker;

    private FraudDetectionService fraudDetectionService;

    private Transaction transaction;
    private FraudRule amountRule;

    @BeforeEach
    void setUp() {
        // Вручную создаём сервис, передавая список моков-checker'ов
        fraudDetectionService = new FraudDetectionService(
                fraudRuleRepository,
                alertRepository,
                auditLogRepository,
                List.of(amountThresholdChecker)
        );

        User user = new User();
        user.setId(1L);

        transaction = new Transaction();
        transaction.setId(100L);
        transaction.setUser(user);
        transaction.setAmount(new BigDecimal("15000.00"));
        transaction.setStatus(TransactionStatus.PENDING);

        amountRule = new FraudRule();
        amountRule.setId(1L);
        amountRule.setRuleType(RuleType.AMOUNT_THRESHOLD);
        amountRule.setThreshold(new BigDecimal("10000.00"));
    }

    @Test
    void shouldCreateAlertWhenRuleTriggers() {
        // given
        when(fraudRuleRepository.findByIsActiveTrue()).thenReturn(List.of(amountRule));
        when(amountThresholdChecker.getSupportedType()).thenReturn(RuleType.AMOUNT_THRESHOLD);
        when(amountThresholdChecker.check(transaction, amountRule)).thenReturn(70);

        // when
        fraudDetectionService.evaluateTransaction(transaction);

        // then
        verify(alertRepository, times(1)).save(any(Alert.class));
        assertEquals(TransactionStatus.FLAGGED, transaction.getStatus());
    }

    @Test
    void shouldNotCreateAlertWhenRuleDoesNotTrigger() {
        when(fraudRuleRepository.findByIsActiveTrue()).thenReturn(List.of(amountRule));
        when(amountThresholdChecker.getSupportedType()).thenReturn(RuleType.AMOUNT_THRESHOLD);
        when(amountThresholdChecker.check(transaction, amountRule)).thenReturn(0);

        fraudDetectionService.evaluateTransaction(transaction);

        verify(alertRepository, never()).save(any(Alert.class));
        assertEquals(TransactionStatus.PENDING, transaction.getStatus());
    }

    @Test
    void shouldAlwaysLogAuditEntryForTransaction() {
        when(fraudRuleRepository.findByIsActiveTrue()).thenReturn(List.of(amountRule));
        when(amountThresholdChecker.getSupportedType()).thenReturn(RuleType.AMOUNT_THRESHOLD);
        when(amountThresholdChecker.check(transaction, amountRule)).thenReturn(0);

        fraudDetectionService.evaluateTransaction(transaction);

        verify(auditLogRepository, atLeastOnce()).save(any(AuditLog.class));
    }
}