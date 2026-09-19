package com.project1.fraud_rules_api.service.rules;

import com.project1.fraud_rules_api.entity.FraudRule;
import com.project1.fraud_rules_api.entity.RuleType;
import com.project1.fraud_rules_api.entity.Transaction;
import com.project1.fraud_rules_api.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class VelocityCheckRuleChecker implements FraudRuleChecker {

    private static final int TIME_WINDOW_MINUTES = 10;

    private final TransactionRepository transactionRepository;

    @Override
    public RuleType getSupportedType() {
        return RuleType.VELOCITY_CHECK;
    }

    @Override
    public int check(Transaction transaction, FraudRule rule) {
        if (rule.getThreshold() == null) {
            return 0;
        }

        LocalDateTime windowStart = LocalDateTime.now().minusMinutes(TIME_WINDOW_MINUTES);

        List<Transaction> recentTransactions = transactionRepository
                .findByUserIdAndCreatedAtAfter(transaction.getUser().getId(), windowStart);

        long count = recentTransactions.stream()
                .filter(t -> !t.getId().equals(transaction.getId())) // исключаем саму себя
                .count();

        if (count > rule.getThreshold().intValue()) {
            return 60;
        }
        return 0;
    }
}