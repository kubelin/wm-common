package com.samsung.wm.constants;

/**
 * 자산관리 상수 클래스
 */
public final class WealthManagementConstants {
    
    private WealthManagementConstants() {
        // 인스턴스 생성 방지
    }
    
    // 위험 성향 상수
    public static final String RISK_PROFILE_CONSERVATIVE = "conservative";
    public static final String RISK_PROFILE_MODERATE = "moderate";
    public static final String RISK_PROFILE_AGGRESSIVE = "aggressive";
    
    // 투자 기간 상수
    public static final String INVESTMENT_PERIOD_SHORT = "short";     // 1년 미만
    public static final String INVESTMENT_PERIOD_MEDIUM = "medium";   // 1-5년
    public static final String INVESTMENT_PERIOD_LONG = "long";       // 5년 이상
    
    // 투자 목표 상수
    public static final String INVESTMENT_GOAL_GROWTH = "growth";
    public static final String INVESTMENT_GOAL_INCOME = "income";
    public static final String INVESTMENT_GOAL_BALANCED = "balanced";
    
    // 자산 유형 상수
    public static final String ASSET_TYPE_STOCK = "STOCK";
    public static final String ASSET_TYPE_BOND = "BOND";
    public static final String ASSET_TYPE_FUND = "FUND";
    public static final String ASSET_TYPE_CASH = "CASH";
    public static final String ASSET_TYPE_ALTERNATIVE = "ALTERNATIVE";
    
    // 고객 등급 상수
    public static final String CUSTOMER_GRADE_VIP = "VIP";
    public static final String CUSTOMER_GRADE_GOLD = "GOLD";
    public static final String CUSTOMER_GRADE_SILVER = "SILVER";
    public static final String CUSTOMER_GRADE_BRONZE = "BRONZE";
    
    // 포트폴리오 상태 상수
    public static final String PORTFOLIO_STATUS_ACTIVE = "active";
    public static final String PORTFOLIO_STATUS_INACTIVE = "inactive";
    public static final String PORTFOLIO_STATUS_SUSPENDED = "suspended";
    
    // 거래 유형 상수
    public static final String TRANSACTION_TYPE_BUY = "BUY";
    public static final String TRANSACTION_TYPE_SELL = "SELL";
    public static final String TRANSACTION_TYPE_DIVIDEND = "DIVIDEND";
    public static final String TRANSACTION_TYPE_REBALANCING = "REBALANCING";
    
    // 최소/최대 값 상수
    public static final int MIN_INVESTMENT_AMOUNT = 10000;
    public static final int MAX_INVESTMENT_AMOUNT = 1000000000;
    public static final double MIN_ALLOCATION_WEIGHT = 0.01;  // 1%
    public static final double MAX_ALLOCATION_WEIGHT = 1.0;   // 100%
    
    // 리밸런싱 임계값
    public static final double REBALANCING_THRESHOLD = 0.05;  // 5%
    public static final int REBALANCING_CHECK_DAYS = 30;      // 30일
}