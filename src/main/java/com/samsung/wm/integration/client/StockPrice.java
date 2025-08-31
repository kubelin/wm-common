package com.samsung.wm.integration.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 주식 가격 정보 클래스
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockPrice {
    
    private String symbol;
    private BigDecimal currentPrice;
    private BigDecimal openPrice;
    private BigDecimal highPrice;
    private BigDecimal lowPrice;
    private Long volume;
}