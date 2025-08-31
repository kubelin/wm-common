package com.samsung.wm.dto.internal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 포트폴리오 분석 내부 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioAnalysisDto {
    
    private String portfolioId;
    private String customerId;
    private BigDecimal totalValue;
    private BigDecimal totalReturn;
    private Double returnRate;
    private Double volatility;
    private Double sharpeRatio;
    private Map<String, Double> assetWeights;
    private Map<String, Double> riskMetrics;
    private LocalDateTime analysisDate;
}