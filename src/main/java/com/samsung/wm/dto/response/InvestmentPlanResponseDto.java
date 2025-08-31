package com.samsung.wm.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 투자 계획 응답 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvestmentPlanResponseDto {
    
    private String customerId;
    private String investmentType;
    private String investmentTypeName;
    private String planName;
    private String description;
    private BigDecimal totalAmount;
    private List<AssetAllocationDto> allocations;
    private LocalDateTime createdAt;
    private String expectedReturn;
    private String riskLevel;
}

/**
 * 자산 배분 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
class AssetAllocationDto {
    private String assetType;
    private String symbol;
    private BigDecimal amount;
    private Double weight;
}