package com.project1.fraud_rules_api.repository;

import com.project1.fraud_rules_api.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUserIdAndCreatedAtAfter(Long userId, LocalDateTime after);

    List<Transaction> findByUserIdAndAmountAndMerchant(Long userId, java.math.BigDecimal amount, String merchant);
}