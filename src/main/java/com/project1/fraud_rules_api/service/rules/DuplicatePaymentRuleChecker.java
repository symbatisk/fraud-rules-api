package com.project1.fraud_rules_api.service.rules;

import com.project1.fraud_rules_api.entity.FraudRule;
import com.project1.fraud_rules_api.entity.RuleType;
import com.project1.fraud_rules_api.entity.Transaction;
import com.project1.fraud_rules_api.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DuplicatePaymentRuleChecker implements FraudRuleChecker {

    private final TransactionRepository transactionRepository;

    @Override
    public RuleType getSupportedType() {
        return RuleType.DUPLICATE_PAYMENT;
    }

    @Override
    public int check(Transaction transaction, FraudRule rule) {
        if (transaction.getMerchant() == null) {
            return 0;
        }

        List<Transaction> duplicates = transactionRepository.findByUserIdAndAmountAndMerchant(
                transaction.getUser().getId(),
                transaction.getAmount(),
                transaction.getMerchant()
        );

        boolean hasDuplicateOtherThanSelf = duplicates.stream()
                .anyMatch(t -> !t.getId().equals(transaction.getId()));

        if (hasDuplicateOtherThanSelf) {
            return 50;
        }
        return 0;
    }
}