package com.project1.fraud_rules_api.service.rules;

import com.project1.fraud_rules_api.entity.FraudRule;
import com.project1.fraud_rules_api.entity.Transaction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BlockedCountryRuleCheckerTest {

    private final BlockedCountryRuleChecker checker = new BlockedCountryRuleChecker();

    @Test
    void shouldReturnRiskScoreWhenCountryIsBlocked() {
        // given
        Transaction transaction = new Transaction();
        transaction.setCountry("KP"); // страна из захардкоженного списка

        FraudRule rule = new FraudRule(); // не используется в логике, но нужен по сигнатуре

        // when
        int riskScore = checker.check(transaction, rule);

        // then
        assertEquals(90, riskScore);
    }

    @Test
    void shouldReturnZeroWhenCountryIsNotBlocked() {
        Transaction transaction = new Transaction();
        transaction.setCountry("US");

        FraudRule rule = new FraudRule();

        int riskScore = checker.check(transaction, rule);

        assertEquals(0, riskScore);
    }
}