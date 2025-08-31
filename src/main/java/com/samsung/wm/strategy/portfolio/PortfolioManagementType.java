package com.samsung.wm.strategy.portfolio;

/**
 * 포트폴리오 관리 유형 열거형
 */
public enum PortfolioManagementType {
    
    REBALANCING("리밸런싱", "목표 비중에 맞춘 포트폴리오 재조정"),
    PERFORMANCE_REVIEW("성과검토", "포트폴리오 성과 분석 및 검토"),
    RISK_MONITORING("위험모니터링", "포트폴리오 위험 수준 모니터링"),
    TAX_OPTIMIZATION("세금최적화", "세금 효율성을 고려한 포트폴리오 관리"),
    DIVIDEND_REINVESTMENT("배당재투자", "배당금 자동 재투자 관리");
    
    private final String displayName;
    private final String description;
    
    PortfolioManagementType(String displayName, String description) {
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