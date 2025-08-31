package com.samsung.wm.strategy.investment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 투자 설계 계획 클래스
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvestmentPlan {
    
    private String customerId;
    private InvestmentType type;
    private String planName;
    private String description;
    private BigDecimal totalAmount;
    private List<AssetAllocation> allocations;
    private Map<String, Object> parameters;
    private LocalDateTime createdAt;
    private String expectedReturn;
    private String riskLevel;
}

