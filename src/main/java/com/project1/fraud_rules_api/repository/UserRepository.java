package com.project1.fraud_rules_api.repository;

import com.project1.fraud_rules_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}