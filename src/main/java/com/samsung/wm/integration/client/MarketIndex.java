package com.samsung.wm.integration.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 시장 지수 정보 클래스
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarketIndex {
    
    private String indexName;
    private BigDecimal currentValue;
    private BigDecimal change;
    private Double changePercent;
}