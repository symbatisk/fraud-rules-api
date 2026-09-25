package com.project1.fraud_rules_api;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI fraudRulesApiOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Fraud Rules API")
                        .description("Backend service that monitors financial transactions and detects suspicious activity using configurable fraud rules (amount thresholds, velocity checks, blocked countries, duplicate payments).")
                        .version("1.0"));
    }
}