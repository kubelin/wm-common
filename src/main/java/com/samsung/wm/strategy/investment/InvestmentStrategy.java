package com.samsung.wm.strategy.investment;

/**
 * 투자설계 전략 인터페이스
 */
public interface InvestmentStrategy {
    
    /**
     * 투자 설계 전략 실행
     * 
     * @param request 투자 요청 정보
     * @return 투자 설계 결과
     */
    InvestmentPlan execute(InvestmentRequest request);
    
    /**
     * 전략 유형 반환
     * 
     * @return 투자 전략 유형
     */
    InvestmentType getType();
    
    /**
     * 해당 고객에게 적합한 전략인지 검증
     * 
     * @param request 투자 요청 정보
     * @return 적합 여부
     */
    boolean isApplicable(InvestmentRequest request);
}