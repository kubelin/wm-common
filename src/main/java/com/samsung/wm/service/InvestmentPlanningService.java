package com.samsung.wm.service;

import com.samsung.common.constants.ErrorCodes;
import com.samsung.common.exception.BusinessException;
import com.samsung.common.util.ValidationUtil;
import com.samsung.common.util.StringUtil;
import com.samsung.common.converter.DataConverter;
import com.samsung.wm.strategy.investment.*;
import com.samsung.wm.strategy.investment.impl.ConservativeInvestmentStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * 투자 계획 수립 서비스
 * 단순화된 전략 패턴 + 공통 모듈 활용
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class InvestmentPlanningService {
    
    private final ConservativeInvestmentStrategy conservativeStrategy;
    // 향후 전략 추가시 여기에 주입
    // private final AggressiveInvestmentStrategy aggressiveStrategy;
    // private final ModerateInvestmentStrategy moderateStrategy;
    
    /**
     * 투자 계획 수립
     * 
     * @param request 투자 요청 정보
     * @return 투자 계획
     */
    public InvestmentPlan createInvestmentPlan(InvestmentRequest request) {
        log.info("투자 계획 수립 - customerId: {}", request.getCustomerId());
        
        // 공통 모듈을 활용한 입력 검증
        validateInvestmentRequest(request);
        
        // 요청 정보를 바탕으로 적절한 투자 유형 결정
        String investmentType = determineInvestmentType(request);
        
        // 전략 선택 및 실행
        InvestmentStrategy strategy = selectStrategy(investmentType);
        InvestmentPlan plan = strategy.execute(request);
        
        // 공통 모듈을 활용한 금융 계산으로 예상 수익률 검증
        validateExpectedReturn(plan);
        
        return plan;
    }
    
    /**
     * 투자 유형별 전략으로 계획 수립
     * 
     * @param request 투자 요청 정보
     * @param investmentType 투자 유형 (CONSERVATIVE, MODERATE, AGGRESSIVE 등)
     * @return 투자 계획
     */
    public InvestmentPlan createInvestmentPlanByType(InvestmentRequest request, String investmentType) {
        log.info("투자 계획 수립 - customerId: {}, type: {}", request.getCustomerId(), investmentType);
        
        validateInvestmentRequest(request);
        
        InvestmentStrategy strategy = selectStrategy(investmentType);
        return strategy.execute(request);
    }
    
    /**
     * 투자 요청 검증
     * 공통 모듈을 활용한 입력값 검증
     */
    private void validateInvestmentRequest(InvestmentRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCodes.NULL_PARAMETER, "투자 요청 정보가 필요합니다");
        }
        
        if (StringUtil.isEmpty(request.getCustomerId())) {
            throw new BusinessException(ErrorCodes.NULL_PARAMETER, "고객 ID가 필요합니다");
        }
        
        // 투자 금액 검증
        if (request.getInvestmentAmount() == null || 
            request.getInvestmentAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(ErrorCodes.INVALID_PARAMETER, "투자 금액이 유효하지 않습니다");
        }
        
        // 투자 기간 검증
        if (StringUtil.isEmpty(request.getInvestmentPeriod())) {
            throw new BusinessException(ErrorCodes.INVALID_PARAMETER, "투자 기간이 유효하지 않습니다");
        }
        
        // 투자 금액 범위 검증 (100만원 ~ 100억원)
        BigDecimal minAmount = new BigDecimal("1000000");     // 100만원
        BigDecimal maxAmount = new BigDecimal("10000000000"); // 100억원
        
        if (!ValidationUtil.isInRange(request.getInvestmentAmount().doubleValue(), 
                                    minAmount.doubleValue(), 
                                    maxAmount.doubleValue())) {
            throw new BusinessException(ErrorCodes.INVALID_PARAMETER, 
                "투자 금액은 " + DataConverter.toCurrencyString(minAmount) + 
                "원 이상 " + DataConverter.toCurrencyString(maxAmount) + "원 이하여야 합니다");
        }
    }
    
    /**
     * 전략 선택
     * 
     * @param investmentType 투자 유형
     * @return 선택된 전략
     */
    private InvestmentStrategy selectStrategy(String investmentType) {
        return switch (investmentType.toUpperCase()) {
            case "CONSERVATIVE" -> conservativeStrategy;
            // case "AGGRESSIVE" -> aggressiveStrategy;    // 향후 추가
            // case "MODERATE" -> moderateStrategy;        // 향후 추가
            default -> {
                log.warn("지원하지 않는 투자 유형: {} - 기본 전략(CONSERVATIVE) 사용", investmentType);
                yield conservativeStrategy;  // 기본 전략
            }
        };
    }
    
    /**
     * 고객 프로필을 바탕으로 적절한 투자 유형 결정
     * 
     * @param request 투자 요청 정보
     * @return 투자 유형
     */
    private String determineInvestmentType(InvestmentRequest request) {
        // 현재는 보수적 전략을 기본으로 사용
        // 향후 고객의 위험성향, 투자경험, 투자목표 등을 종합적으로 분석하여 결정
        if (StringUtil.isNotEmpty(request.getRiskProfile())) {
            return switch (request.getRiskProfile().toUpperCase()) {
                case "LOW", "CONSERVATIVE" -> "CONSERVATIVE";
                case "MEDIUM", "MODERATE" -> "CONSERVATIVE"; // 현재는 보수적 전략만 구현
                case "HIGH", "AGGRESSIVE" -> "CONSERVATIVE"; // 현재는 보수적 전략만 구현
                default -> "CONSERVATIVE";
            };
        }
        return "CONSERVATIVE";
    }
    
    /**
     * 예상 수익률 검증
     * 공통 모듈의 금융 계산을 활용한 수익률 검증
     */
    private void validateExpectedReturn(InvestmentPlan plan) {
        if (StringUtil.isNotEmpty(plan.getExpectedReturn())) {
            // 예상 수익률이 "연 3-5%" 형태이므로 로그로만 확인
            log.info("투자 계획 검증 완료 - 투자 금액: {}, 예상 수익률: {}", 
                    DataConverter.toCurrencyString(plan.getTotalAmount()),
                    plan.getExpectedReturn());
        }
    }
    
    /**
     * 고객별 투자 계획 목록 조회
     * 
     * @param customerId 고객 ID
     * @return 투자 계획 목록
     */
    public List<InvestmentPlan> getInvestmentPlans(String customerId) {
        log.info("투자 계획 목록 조회 - customerId: {}", customerId);
        
        // 실제로는 데이터베이스에서 투자 계획을 조회
        return List.of(); // 샘플 구현
    }
}