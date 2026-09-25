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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DuplicatePaymentRuleCheckerTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private DuplicatePaymentRuleChecker checker;

    @Test
    void shouldReturnRiskScoreWhenDuplicateOtherThanSelfExists() {
        // given
        User user = new User();
        user.setId(1L);

        Transaction currentTransaction = new Transaction();
        currentTransaction.setId(100L);
        currentTransaction.setUser(user);
        currentTransaction.setAmount(new BigDecimal("100.00"));
        currentTransaction.setMerchant("Amazon");

        FraudRule rule = new FraudRule();

        Transaction realDuplicate = new Transaction();
        realDuplicate.setId(50L);

        Transaction self = new Transaction();
        self.setId(100L);

        when(transactionRepository.findByUserIdAndAmountAndMerchant(anyLong(), any(), any()))
                .thenReturn(List.of(realDuplicate, self));

        // when
        int riskScore = checker.check(currentTransaction, rule);

        // then
        assertEquals(50, riskScore);
    }

    @Test
    void shouldReturnZeroWhenOnlySelfFoundNoDuplicates() {
        User user = new User();
        user.setId(1L);

        Transaction currentTransaction = new Transaction();
        currentTransaction.setId(100L);
        currentTransaction.setUser(user);
        currentTransaction.setAmount(new BigDecimal("100.00"));
        currentTransaction.setMerchant("Amazon");

        FraudRule rule = new FraudRule();

        Transaction self = new Transaction();
        self.setId(100L);

        when(transactionRepository.findByUserIdAndAmountAndMerchant(anyLong(), any(), any()))
                .thenReturn(List.of(self));

        int riskScore = checker.check(currentTransaction, rule);

        assertEquals(0, riskScore);
    }

    @Test
    void shouldReturnZeroWhenNoDuplicatesFoundAtAll() {
        User user = new User();
        user.setId(1L);

        Transaction currentTransaction = new Transaction();
        currentTransaction.setId(100L);
        currentTransaction.setUser(user);
        currentTransaction.setAmount(new BigDecimal("100.00"));
        currentTransaction.setMerchant("Amazon");

        FraudRule rule = new FraudRule();

        when(transactionRepository.findByUserIdAndAmountAndMerchant(anyLong(), any(), any()))
                .thenReturn(List.of());

        int riskScore = checker.check(currentTransaction, rule);

        assertEquals(0, riskScore);
    }

    @Test
    void shouldReturnZeroWhenMerchantIsNull() {
        User user = new User();
        user.setId(1L);

        Transaction currentTransaction = new Transaction();
        currentTransaction.setId(100L);
        currentTransaction.setUser(user);
        currentTransaction.setAmount(new BigDecimal("100.00"));
        currentTransaction.setMerchant(null);

        FraudRule rule = new FraudRule();

        int riskScore = checker.check(currentTransaction, rule);

        assertEquals(0, riskScore);
        verifyNoInteractions(transactionRepository);
    }
}