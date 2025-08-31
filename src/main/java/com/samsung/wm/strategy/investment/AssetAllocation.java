package com.samsung.wm.strategy.investment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 자산 배분 정보
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssetAllocation {
    private String assetType; // stock, bond, fund, etc.
    private String symbol;
    private BigDecimal amount;
    private Double weight; // percentage
}