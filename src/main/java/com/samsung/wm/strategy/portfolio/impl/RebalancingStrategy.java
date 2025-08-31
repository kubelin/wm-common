package com.samsung.wm.strategy.portfolio.impl;

import com.samsung.wm.strategy.portfolio.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 리밸런싱 전략 구현체
 */
@Slf4j
@Component
public class RebalancingStrategy implements PortfolioStrategy {
    
    @Override
    public PortfolioManagementResult execute(PortfolioRequest request) {
        log.info("리밸런싱 전략 실행 - portfolioId: {}", request.getPortfolioId());
        
        List<String> actions = Arrays.asList(
            "주식 비중 5% 감소",
            "채권 비중 3% 증가",
            "현금 비중 2% 증가"
        );
        
        Map<String, Object> details = new HashMap<>();
        details.put("previousAllocation", Map.of("stock", 60, "bond", 30, "cash", 10));
        details.put("newAllocation", Map.of("stock", 55, "bond", 33, "cash", 12));
        details.put("rebalancingReason", "목표 비중 대비 편차 초과");
        
        return new PortfolioManagementResult(
            request.getPortfolioId(),
            request.getCustomerId(),
            PortfolioManagementType.REBALANCING,
            "포트폴리오 리밸런싱이 성공적으로 완료되었습니다.",
            actions,
            details,
            LocalDateTime.now(),
            true
        );
    }
    
    @Override
    public PortfolioManagementType getType() {
        return PortfolioManagementType.REBALANCING;
    }
    
    @Override
    public boolean needsRebalancing(Portfolio portfolio) {
        // 리밸런싱 필요 여부 판단 로직
        // 실제로는 목표 비중과 현재 비중의 차이를 계산하여 판단
        return true; // 샘플 구현
    }
}