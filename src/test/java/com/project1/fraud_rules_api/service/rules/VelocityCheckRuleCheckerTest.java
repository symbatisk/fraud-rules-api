package com.project1.fraud_rules_api.service.rules;

import com.project1.fraud_rules_api.entity.FraudRule;
import com.project1.fraud_rules_api.entity.Transaction;
import com.project1.fraud_rules_api.entity.User;
import com.project1.fraud_rules_api.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VelocityCheckRuleCheckerTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private VelocityCheckRuleChecker checker;

    private Transaction createTransactionWithId(Long id) {
        Transaction t = new Transaction();
        t.setId(id);
        return t;
    }

    @Test
    void shouldReturnRiskScoreWhenTooManyRecentTransactionsExcludingSelf() {
        // given
        User user = new User();
        user.setId(1L);

        Transaction currentTransaction = new Transaction();
        currentTransaction.setId(100L);
        currentTransaction.setUser(user);

        FraudRule rule = new FraudRule();
        rule.setThreshold(new BigDecimal("5")); // максимум 5 транзакций

        List<Transaction> recentTransactions = new ArrayList<>(List.of(
                createTransactionWithId(1L),
                createTransactionWithId(2L),
                createTransactionWithId(3L),
                createTransactionWithId(4L),
                createTransactionWithId(5L),
                createTransactionWithId(6L),
                createTransactionWithId(100L) // это сама текущая транзакция
        ));

        when(transactionRepository.findByUserIdAndCreatedAtAfter(anyLong(), any()))
                .thenReturn(recentTransactions);

        // when
        int riskScore = checker.check(currentTransaction, rule);

        // then — после исключения "себя" остаётся 6 транзакций, что больше threshold=5
        assertEquals(60, riskScore);
    }

    @Test
    void shouldReturnZeroWhenTransactionCountIsWithinLimitExcludingSelf() {
        User user = new User();
        user.setId(1L);

        Transaction currentTransaction = new Transaction();
        currentTransaction.setId(100L);
        currentTransaction.setUser(user);

        FraudRule rule = new FraudRule();
        rule.setThreshold(new BigDecimal("5"));

        List<Transaction> recentTransactions = new ArrayList<>(List.of(
                createTransactionWithId(1L),
                createTransactionWithId(2L),
                createTransactionWithId(100L)
        ));

        when(transactionRepository.findByUserIdAndCreatedAtAfter(anyLong(), any()))
                .thenReturn(recentTransactions);

        int riskScore = checker.check(currentTransaction, rule);

        assertEquals(0, riskScore);
    }
}