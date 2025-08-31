package com.samsung.wm.strategy.investment;

/**
 * 투자 전략 유형 열거형
 */
public enum InvestmentType {
    
    CONSERVATIVE("보수형", "안정성을 중시하는 투자 전략"),
    MODERATE("중도형", "안정성과 수익성의 균형을 추구하는 전략"),
    AGGRESSIVE("적극형", "고수익을 목표로 하는 적극적 투자 전략"),
    GROWTH_ORIENTED("성장형", "성장 가능성이 높은 종목 중심 전략"),
    INCOME_FOCUSED("수익형", "배당이나 이자 수익에 중점을 둔 전략"),
    ESG("ESG", "환경, 사회, 지배구조를 고려한 지속가능 투자 전략");
    
    private final String displayName;
    private final String description;
    
    InvestmentType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getDescription() {
        return description;
    }
}