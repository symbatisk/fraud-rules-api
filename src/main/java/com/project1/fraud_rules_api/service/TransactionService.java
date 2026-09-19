package com.project1.fraud_rules_api.service;

import com.project1.fraud_rules_api.dto.TransactionRequest;
import com.project1.fraud_rules_api.dto.TransactionResponse;
import com.project1.fraud_rules_api.entity.Transaction;
import com.project1.fraud_rules_api.entity.TransactionStatus;
import com.project1.fraud_rules_api.entity.User;
import com.project1.fraud_rules_api.exception.ResourceNotFoundException;
import com.project1.fraud_rules_api.repository.TransactionRepository;
import com.project1.fraud_rules_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final FraudDetectionService fraudDetectionService;

    public TransactionResponse createTransaction(TransactionRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + request.getUserId()));

        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setAmount(request.getAmount());
        transaction.setCurrency(request.getCurrency());
        transaction.setCountry(request.getCountry());
        transaction.setMerchant(request.getMerchant());
        transaction.setStatus(TransactionStatus.PENDING);

        Transaction saved = transactionRepository.save(transaction);

        fraudDetectionService.evaluateTransaction(saved);

        Transaction updated = transactionRepository.save(saved);

        return toResponse(saved);
    }

    public List<TransactionResponse> getAllTransactions() {
        return transactionRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private TransactionResponse toResponse(Transaction t) {
        return new TransactionResponse(
                t.getId(),
                t.getUser().getId(),
                t.getAmount(),
                t.getCurrency(),
                t.getCountry(),
                t.getMerchant(),
                t.getStatus(),
                t.getCreatedAt()
        );
    }
}