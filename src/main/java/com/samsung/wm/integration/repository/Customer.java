package com.samsung.wm.integration.repository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 고객 엔티티 클래스
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    
    private String customerId;
    private String name;
    private String email;
    private String phoneNumber;
    private LocalDate birthDate;
    private String riskProfile; // conservative, moderate, aggressive
    private BigDecimal totalAssets;
    private String customerGrade; // VIP, GOLD, SILVER, BRONZE
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean active;
}