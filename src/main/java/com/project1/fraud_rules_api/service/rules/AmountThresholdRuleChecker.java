package com.project1.fraud_rules_api.service.rules;

import com.project1.fraud_rules_api.entity.FraudRule;
import com.project1.fraud_rules_api.entity.RuleType;
import com.project1.fraud_rules_api.entity.Transaction;
import org.springframework.stereotype.Component;

@Component
public class AmountThresholdRuleChecker implements FraudRuleChecker {

    @Override
    public RuleType getSupportedType() {
        return RuleType.AMOUNT_THRESHOLD;
    }

    @Override
    public int check(Transaction transaction, FraudRule rule) {
        if (rule.getThreshold() == null) {
            return 0;
        }
        if (transaction.getAmount().compareTo(rule.getThreshold()) > 0) {
            return 70; // risk score — можно потом сделать умнее (чем больше превышение, тем выше score)
        }
        return 0;
    }
}