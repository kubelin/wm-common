package com.samsung.wm.dto.internal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 시장 데이터 내부 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarketDataDto {
    
    private String symbol;
    private BigDecimal currentPrice;
    private BigDecimal openPrice;
    private BigDecimal highPrice;
    private BigDecimal lowPrice;
    private Long volume;
    private BigDecimal previousClose;
    private BigDecimal change;
    private Double changePercent;
    private LocalDateTime lastUpdated;
}