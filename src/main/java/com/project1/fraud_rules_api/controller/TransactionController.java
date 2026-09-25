package com.project1.fraud_rules_api.controller;

import com.project1.fraud_rules_api.dto.TransactionRequest;
import com.project1.fraud_rules_api.dto.TransactionResponse;
import com.project1.fraud_rules_api.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@Tag(name = "Transactions", description = "Create and view financial transactions; each transaction is automatically evaluated against active fraud rules")
public class TransactionController {

    private final TransactionService transactionService;

    @Operation(summary = "Create a new transaction", description = "Creates a transaction and immediately runs it through all active fraud rules. If any rule triggers, an alert is created and the transaction status becomes FLAGGED.")
    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(@Valid @RequestBody TransactionRequest request) {
        TransactionResponse response = transactionService.createTransaction(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Get all transactions", description = "Returns all transactions in the system, including their current status (PENDING, APPROVED, FLAGGED)")
    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }
}