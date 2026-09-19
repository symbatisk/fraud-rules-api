package com.project1.fraud_rules_api.service.rules;

import com.project1.fraud_rules_api.entity.FraudRule;
import com.project1.fraud_rules_api.entity.RuleType;
import com.project1.fraud_rules_api.entity.Transaction;

public interface FraudRuleChecker {

    RuleType getSupportedType();


    int check(Transaction transaction, FraudRule rule);
}