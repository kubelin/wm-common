package com.samsung.wm.strategy.consultation;

/**
 * 고객상담 전략 인터페이스
 */
public interface ConsultationStrategy {
    
    /**
     * 고객 상담 전략 실행
     * 
     * @param customerId 고객 ID
     * @return 상담 결과
     */
    ConsultationResult execute(String customerId);
    
    /**
     * 전략 유형 반환
     * 
     * @return 전략 유형
     */
    ConsultationType getType();
}