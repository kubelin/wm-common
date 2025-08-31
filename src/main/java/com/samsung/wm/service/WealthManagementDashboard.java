package com.samsung.wm.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 자산관리 대시보드 정보 클래스
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WealthManagementDashboard {
    
    private String customerId;
    private String customerName;
    private BigDecimal totalAssets;
    private BigDecimal totalReturn;
    private Double returnRate;
    private String riskLevel;
    
    private List<PortfolioSummary> portfolioSummaries;
    private List<RecentTransaction> recentTransactions;
    private Map<String, BigDecimal> assetAllocation;
    
    private LocalDateTime lastUpdated;
}

/**
 * 포트폴리오 요약 정보
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
class PortfolioSummary {
    private String portfolioId;
    private String name;
    private BigDecimal value;
    private BigDecimal return_;
    private Double returnRate;
}

/**
 * 최근 거래 정보
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
class RecentTransaction {
    private String transactionId;
    private String type; // BUY, SELL
    private String symbol;
    private Integer quantity;
    private BigDecimal price;
    private LocalDateTime transactionDate;
}