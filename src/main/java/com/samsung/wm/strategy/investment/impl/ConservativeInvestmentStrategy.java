package com.samsung.wm.strategy.investment.impl;

import com.samsung.wm.strategy.investment.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 보수형 투자 전략 구현체
 */
@Slf4j
@Component
public class ConservativeInvestmentStrategy implements InvestmentStrategy {
    
    @Override
    public InvestmentPlan execute(InvestmentRequest request) {
        log.info("보수형 투자 전략 실행 - customerId: {}", request.getCustomerId());
        
        // 보수형 투자 계획 생성
        List<AssetAllocation> allocations = createConservativeAllocations(request.getInvestmentAmount());
        
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("maxStockRatio", 30);
        parameters.put("minBondRatio", 50);
        parameters.put("riskLevel", "LOW");
        
        return new InvestmentPlan(
            request.getCustomerId(),
            InvestmentType.CONSERVATIVE,
            "안정형 자산배분 포트폴리오",
            "안정성을 중시하는 보수적 투자 전략으로 채권 비중을 높게 구성",
            request.getInvestmentAmount(),
            allocations,
            parameters,
            LocalDateTime.now(),
            "연 3-5%",
            "낮음"
        );
    }
    
    @Override
    public InvestmentType getType() {
        return InvestmentType.CONSERVATIVE;
    }
    
    @Override
    public boolean isApplicable(InvestmentRequest request) {
        return "conservative".equalsIgnoreCase(request.getRiskProfile());
    }
    
    private List<AssetAllocation> createConservativeAllocations(BigDecimal totalAmount) {
        return Arrays.asList(
            new AssetAllocation("BOND", "KTB_10Y", totalAmount.multiply(new BigDecimal("0.5")), 50.0),
            new AssetAllocation("BOND", "CORP_BOND", totalAmount.multiply(new BigDecimal("0.2")), 20.0),
            new AssetAllocation("STOCK", "KODEX200", totalAmount.multiply(new BigDecimal("0.2")), 20.0),
            new AssetAllocation("CASH", "MMF", totalAmount.multiply(new BigDecimal("0.1")), 10.0)
        );
    }
}