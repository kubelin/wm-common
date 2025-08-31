package com.samsung.wm.strategy.consultation;

/**
 * 고객 상담 유형 열거형
 */
public enum ConsultationType {
    
    INITIAL("초기상담", "신규 고객 대상 초기 상담"),
    PORTFOLIO_REVIEW("포트폴리오검토", "기존 포트폴리오 성과 검토 상담"),
    RISK_ASSESSMENT("위험성향평가", "고객 위험성향 재평가 상담"),
    INVESTMENT_PLAN("투자계획", "투자 계획 수립 상담"),
    REBALANCING("리밸런싱", "포트폴리오 재조정 상담");
    
    private final String displayName;
    private final String description;
    
    ConsultationType(String displayName, String description) {
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