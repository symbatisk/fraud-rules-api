package com.project1.fraud_rules_api.service.rules;

import com.project1.fraud_rules_api.entity.FraudRule;
import com.project1.fraud_rules_api.entity.RuleType;
import com.project1.fraud_rules_api.entity.Transaction;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AmountThresholdRuleCheckerTest {

    private final AmountThresholdRuleChecker checker = new AmountThresholdRuleChecker();

    @Test
    void shouldReturnRiskScoreWhenAmountExceedsThreshold() {
        // given
        FraudRule rule = new FraudRule();
        rule.setThreshold(new BigDecimal("10000.00"));

        Transaction transaction = new Transaction();
        transaction.setAmount(new BigDecimal("15000.00"));

        // when
        int riskScore = checker.check(transaction, rule);

        // then
        assertEquals(70, riskScore);
    }

    @Test
    void shouldReturnZeroWhenAmountIsBelowThreshold() {
        FraudRule rule = new FraudRule();
        rule.setThreshold(new BigDecimal("10000.00"));

        Transaction transaction = new Transaction();
        transaction.setAmount(new BigDecimal("500.00"));

        int riskScore = checker.check(transaction, rule);

        assertEquals(0, riskScore);
    }

    @Test
    void shouldReturnZeroWhenThresholdIsNull() {
        FraudRule rule = new FraudRule();
        rule.setThreshold(null);

        Transaction transaction = new Transaction();
        transaction.setAmount(new BigDecimal("999999.00"));

        int riskScore = checker.check(transaction, rule);

        assertEquals(0, riskScore);
    }
}