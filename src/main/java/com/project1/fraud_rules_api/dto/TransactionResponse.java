package com.project1.fraud_rules_api.dto;

import com.project1.fraud_rules_api.entity.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class TransactionResponse {
    private Long id;
    private Long userId;
    private BigDecimal amount;
    private String currency;
    private String country;
    private String merchant;
    private TransactionStatus status;
    private LocalDateTime createdAt;
}