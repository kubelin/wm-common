package com.samsung.wm.dto.internal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 고객 프로필 내부 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerProfileDto {
    
    private String customerId;
    private String name;
    private String email;
    private String phoneNumber;
    private LocalDate birthDate;
    private String riskProfile;
    private BigDecimal totalAssets;
    private String customerGrade;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean active;
}