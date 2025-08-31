package com.samsung.wm.service;

import com.samsung.wm.strategy.consultation.ConsultationResult;
import com.samsung.wm.strategy.investment.InvestmentPlan;
import com.samsung.wm.strategy.investment.InvestmentRequest;
import com.samsung.wm.strategy.portfolio.PortfolioManagementResult;
import com.samsung.wm.strategy.portfolio.PortfolioRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 자산관리 통합 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WealthManagementService {
    
    private final ConsultationService consultationService;
    private final InvestmentPlanningService investmentPlanningService;
    private final PortfolioManagementService portfolioManagementService;
    
    /**
     * 고객 초기 상담 실행
     * 
     * @param customerId 고객 ID
     * @return 상담 결과
     */
    public ConsultationResult conductInitialConsultation(String customerId) {
        log.info("고객 초기 상담 시작 - customerId: {}", customerId);
        return consultationService.conductConsultation(customerId, "INITIAL");
    }
    
    /**
     * 투자 계획 수립
     * 
     * @param request 투자 요청 정보
     * @return 투자 계획
     */
    public InvestmentPlan createInvestmentPlan(InvestmentRequest request) {
        log.info("투자 계획 수립 - customerId: {}", request.getCustomerId());
        return investmentPlanningService.createInvestmentPlan(request);
    }
    
    /**
     * 포트폴리오 리밸런싱 실행
     * 
     * @param request 포트폴리오 관리 요청
     * @return 관리 결과
     */
    public PortfolioManagementResult executeRebalancing(PortfolioRequest request) {
        log.info("포트폴리오 리밸런싱 실행 - portfolioId: {}", request.getPortfolioId());
        return portfolioManagementService.managePortfolio(request);
    }
    
    /**
     * 통합 자산관리 대시보드 정보 생성
     * 
     * @param customerId 고객 ID
     * @return 대시보드 정보
     */
    public WealthManagementDashboard generateDashboard(String customerId) {
        log.info("자산관리 대시보드 생성 - customerId: {}", customerId);
        
        // 각 서비스로부터 정보를 수집하여 통합 대시보드 생성
        return WealthManagementDashboard.builder()
            .customerId(customerId)
            .build();
    }
}