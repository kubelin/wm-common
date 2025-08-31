package com.samsung.wm.constants;

/**
 * 메시지 상수 클래스
 */
public final class MessageConstants {
    
    private MessageConstants() {
        // 인스턴스 생성 방지
    }
    
    // 성공 메시지
    public static final String SUCCESS_CONSULTATION_COMPLETED = "상담이 성공적으로 완료되었습니다.";
    public static final String SUCCESS_INVESTMENT_PLAN_CREATED = "투자 계획이 성공적으로 생성되었습니다.";
    public static final String SUCCESS_PORTFOLIO_REBALANCED = "포트폴리오 리밸런싱이 완료되었습니다.";
    public static final String SUCCESS_CUSTOMER_REGISTERED = "고객이 성공적으로 등록되었습니다.";
    public static final String SUCCESS_PORTFOLIO_CREATED = "포트폴리오가 성공적으로 생성되었습니다.";
    
    // 오류 메시지
    public static final String ERROR_CUSTOMER_NOT_FOUND = "고객 정보를 찾을 수 없습니다.";
    public static final String ERROR_PORTFOLIO_NOT_FOUND = "포트폴리오를 찾을 수 없습니다.";
    public static final String ERROR_INVALID_RISK_PROFILE = "유효하지 않은 위험 성향입니다.";
    public static final String ERROR_INVALID_INVESTMENT_AMOUNT = "유효하지 않은 투자 금액입니다.";
    public static final String ERROR_UNSUPPORTED_STRATEGY = "지원하지 않는 전략입니다.";
    public static final String ERROR_INSUFFICIENT_BALANCE = "잔고가 부족합니다.";
    public static final String ERROR_MARKET_DATA_UNAVAILABLE = "시장 데이터를 가져올 수 없습니다.";
    
    // 검증 메시지
    public static final String VALIDATION_REQUIRED_CUSTOMER_ID = "고객 ID는 필수입니다.";
    public static final String VALIDATION_REQUIRED_PORTFOLIO_ID = "포트폴리오 ID는 필수입니다.";
    public static final String VALIDATION_REQUIRED_INVESTMENT_AMOUNT = "투자 금액은 필수입니다.";
    public static final String VALIDATION_MIN_INVESTMENT_AMOUNT = "최소 투자 금액은 1만원입니다.";
    public static final String VALIDATION_REQUIRED_RISK_PROFILE = "위험 성향은 필수입니다.";
    
    // 알림 메시지
    public static final String NOTIFICATION_REBALANCING_NEEDED = "포트폴리오 리밸런싱이 필요합니다.";
    public static final String NOTIFICATION_PERFORMANCE_ALERT = "포트폴리오 성과에 주의가 필요합니다.";
    public static final String NOTIFICATION_RISK_ALERT = "위험 수준이 목표를 초과했습니다.";
    public static final String NOTIFICATION_CONSULTATION_DUE = "정기 상담 일정이 다가왔습니다.";
    
    // 상태 메시지
    public static final String STATUS_PROCESSING = "처리 중입니다.";
    public static final String STATUS_COMPLETED = "완료되었습니다.";
    public static final String STATUS_FAILED = "실패했습니다.";
    public static final String STATUS_PENDING = "대기 중입니다.";
}