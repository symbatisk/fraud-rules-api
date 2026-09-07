package com.project1.fraud_rules_api.repository;

import com.project1.fraud_rules_api.entity.FraudRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FraudRuleRepository extends JpaRepository<FraudRule, Long> {
    List<FraudRule> findByIsActiveTrue();
}