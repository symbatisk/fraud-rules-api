package com.project1.fraud_rules_api.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransactionRequest {

    @NotNull(message = "userId is required")
    private Long userId;

    @NotNull(message = "amount is required")
    @DecimalMin(value = "0.01", message = "amount must be positive")
    private BigDecimal amount;

    @NotBlank(message = "currency is required")
    @Size(min = 3, max = 3, message = "currency must be a 3-letter code")
    private String currency;

    @NotBlank(message = "country is required")
    @Size(min = 2, max = 2, message = "country must be a 2-letter code")
    private String country;

    private String merchant;
}