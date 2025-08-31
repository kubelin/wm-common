package com.samsung.wm.service;

import com.samsung.common.constants.ErrorCodes;
import com.samsung.common.exception.BusinessException;
import com.samsung.common.util.StringUtil;
import com.samsung.wm.strategy.portfolio.*;
import com.samsung.wm.strategy.portfolio.impl.RebalancingStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 포트폴리오 관리 서비스
 * 단순화된 전략 패턴 적용 (Factory 패턴 제거)
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PortfolioManagementService {
    
    private final RebalancingStrategy rebalancingStrategy;
    // 향후 전략 추가시 여기에 주입
    // private final OptimizationStrategy optimizationStrategy;
    // private final RiskManagementStrategy riskManagementStrategy;
    
    /**
     * 포트폴리오 관리 실행
     * 
     * @param request 포트폴리오 관리 요청
     * @return 관리 결과
     */
    public PortfolioManagementResult managePortfolio(PortfolioRequest request) {
        log.info("포트폴리오 관리 실행 - portfolioId: {}, type: {}", 
                request.getPortfolioId(), request.getManagementType());
        
        // 공통 모듈을 활용한 입력 검증
        validatePortfolioRequest(request);
        
        // 간단한 조건문으로 전략 선택 (Factory 패턴 대신)
        PortfolioStrategy strategy = selectStrategy(request.getManagementType());
        
        // 전략 실행
        return strategy.execute(request);
    }
    
    /**
     * 포트폴리오 관리 요청 검증
     * 공통 모듈을 활용한 입력값 검증
     */
    private void validatePortfolioRequest(PortfolioRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCodes.NULL_PARAMETER, "포트폴리오 관리 요청 정보가 필요합니다");
        }
        
        if (StringUtil.isEmpty(request.getPortfolioId())) {
            throw new BusinessException(ErrorCodes.NULL_PARAMETER, "포트폴리오 ID가 필요합니다");
        }
        
        if (request.getManagementType() == null) {
            throw new BusinessException(ErrorCodes.NULL_PARAMETER, "관리 유형이 필요합니다");
        }
    }
    
    /**
     * 관리 유형에 따른 전략 선택
     * 
     * @param managementType 관리 유형
     * @return 선택된 전략
     */
    private PortfolioStrategy selectStrategy(PortfolioManagementType managementType) {
        return switch (managementType) {
            case REBALANCING -> rebalancingStrategy;
            // case OPTIMIZATION -> optimizationStrategy;     // 향후 추가
            // case RISK_MANAGEMENT -> riskManagementStrategy; // 향후 추가
            default -> {
                log.warn("지원하지 않는 관리 유형: {} - 기본 전략(REBALANCING) 사용", managementType);
                yield rebalancingStrategy;  // 기본 전략
            }
        };
    }
    
    /**
     * 포트폴리오 목록 조회
     * 
     * @param customerId 고객 ID
     * @return 포트폴리오 목록
     */
    public List<Portfolio> getPortfolios(String customerId) {
        log.info("포트폴리오 목록 조회 - customerId: {}", customerId);
        
        // 공통 모듈을 활용한 입력 검증
        if (StringUtil.isEmpty(customerId)) {
            throw new BusinessException(ErrorCodes.NULL_PARAMETER, "고객 ID가 필요합니다");
        }
        
        // 실제로는 데이터베이스에서 포트폴리오를 조회
        return List.of(); // 샘플 구현
    }
    
    /**
     * 리밸런싱 필요 포트폴리오 검색
     * 
     * @return 리밸런싱 필요 포트폴리오 목록
     */
    public List<Portfolio> findPortfoliosNeedingRebalancing() {
        log.info("리밸런싱 필요 포트폴리오 검색");
        
        // 실제로는 각 포트폴리오에 대해 리밸런싱 필요성 검사
        return List.of(); // 샘플 구현
    }
}