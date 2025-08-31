package com.samsung.wm.strategy.portfolio;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 포트폴리오 정보 클래스
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Portfolio {
    
    private String portfolioId;
    private String customerId;
    private String name;
    private BigDecimal totalValue;
    private List<Holding> holdings;
    private LocalDateTime lastUpdated;
    private String status; // active, inactive, suspended
}

/**
 * 보유 종목 정보
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
class Holding {
    private String symbol;
    private String assetType;
    private Integer quantity;
    private BigDecimal avgPrice;
    private BigDecimal currentPrice;
    private BigDecimal marketValue;
    private Double weight;
}