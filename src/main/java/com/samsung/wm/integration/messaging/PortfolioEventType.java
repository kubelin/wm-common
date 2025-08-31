package com.samsung.wm.integration.messaging;

/**
 * 포트폴리오 이벤트 유형 열거형
 */
public enum PortfolioEventType {
    
    PORTFOLIO_CREATED("포트폴리오 생성"),
    PORTFOLIO_UPDATED("포트폴리오 업데이트"),
    PORTFOLIO_DELETED("포트폴리오 삭제"),
    REBALANCING_STARTED("리밸런싱 시작"),
    REBALANCING_COMPLETED("리밸런싱 완료"),
    PERFORMANCE_ALERT("성과 알림"),
    RISK_ALERT("리스크 알림");
    
    private final String description;
    
    PortfolioEventType(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}