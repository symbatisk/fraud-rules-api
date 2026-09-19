package com.project1.fraud_rules_api.service.rules;

import com.project1.fraud_rules_api.entity.FraudRule;
import com.project1.fraud_rules_api.entity.RuleType;
import com.project1.fraud_rules_api.entity.Transaction;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class BlockedCountryRuleChecker implements FraudRuleChecker {

    private static final Set<String> BLOCKED_COUNTRIES = Set.of("KP", "IR", "SY");

    @Override
    public RuleType getSupportedType() {
        return RuleType.BLOCKED_COUNTRY;
    }

    @Override
    public int check(Transaction transaction, FraudRule rule) {
        if (BLOCKED_COUNTRIES.contains(transaction.getCountry())) {
            return 90;
        }
        return 0;
    }
}