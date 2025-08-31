package com.samsung.wm.strategy.portfolio;

/**
 * 포트폴리오 관리 전략 인터페이스
 */
public interface PortfolioStrategy {
    
    /**
     * 포트폴리오 관리 전략 실행
     * 
     * @param request 포트폴리오 관리 요청
     * @return 포트폴리오 관리 결과
     */
    PortfolioManagementResult execute(PortfolioRequest request);
    
    /**
     * 전략 유형 반환
     * 
     * @return 포트폴리오 관리 유형
     */
    PortfolioManagementType getType();
    
    /**
     * 리밸런싱 필요 여부 확인
     * 
     * @param portfolio 현재 포트폴리오
     * @return 리밸런싱 필요 여부
     */
    boolean needsRebalancing(Portfolio portfolio);
}