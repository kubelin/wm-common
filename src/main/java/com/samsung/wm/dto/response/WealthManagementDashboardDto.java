package com.samsung.wm.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 자산관리 대시보드 응답 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WealthManagementDashboardDto {
    
    private String customerId;
    private String customerName;
    private BigDecimal totalAssets;
    private BigDecimal totalReturn;
    private Double returnRate;
    private String riskLevel;
    
    private List<PortfolioSummaryDto> portfolioSummaries;
    private List<RecentTransactionDto> recentTransactions;
    private Map<String, BigDecimal> assetAllocation;
    
    private LocalDateTime lastUpdated;
}

/**
 * 포트폴리오 요약 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
class PortfolioSummaryDto {
    private String portfolioId;
    private String name;
    private BigDecimal value;
    private BigDecimal return_;
    private Double returnRate;
}

/**
 * 최근 거래 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
class RecentTransactionDto {
    private String transactionId;
    private String type;
    private String symbol;
    private Integer quantity;
    private BigDecimal price;
    private LocalDateTime transactionDate;
}