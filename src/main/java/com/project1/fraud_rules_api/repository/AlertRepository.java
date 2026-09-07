package com.project1.fraud_rules_api.repository;

import com.project1.fraud_rules_api.entity.Alert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertRepository extends JpaRepository<Alert, Long> {
}