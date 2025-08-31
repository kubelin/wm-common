package com.samsung.wm.strategy.consultation.impl;

import com.samsung.wm.strategy.consultation.ConsultationResult;
import com.samsung.wm.strategy.consultation.ConsultationStrategy;
import com.samsung.wm.strategy.consultation.ConsultationType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 초기 고객 상담 전략 구현체
 */
@Slf4j
@Component
public class InitialConsultationStrategy implements ConsultationStrategy {
    
    @Override
    public ConsultationResult execute(String customerId) {
        log.info("초기 상담 전략 실행 - customerId: {}", customerId);
        
        // 초기 상담 로직 구현
        Map<String, Object> details = new HashMap<>();
        details.put("riskProfile", "conservative");
        details.put("investmentGoal", "장기투자");
        details.put("initialAmount", 10000000);
        
        return new ConsultationResult(
            customerId,
            ConsultationType.INITIAL,
            "초기 상담이 성공적으로 완료되었습니다.",
            details,
            LocalDateTime.now(),
            true
        );
    }
    
    @Override
    public ConsultationType getType() {
        return ConsultationType.INITIAL;
    }
}