package com.samsung.wm.strategy.investment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 투자 요청 정보 클래스
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvestmentRequest {
    
    private String customerId;
    private BigDecimal investmentAmount;
    private String riskProfile; // conservative, moderate, aggressive
    private String investmentPeriod; // short, medium, long
    private String investmentGoal; // growth, income, balanced
    private boolean hasExistingPortfolio;
}