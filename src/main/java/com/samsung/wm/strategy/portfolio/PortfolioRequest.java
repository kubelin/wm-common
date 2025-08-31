package com.samsung.wm.strategy.portfolio;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 포트폴리오 관리 요청 클래스
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioRequest {
    
    private String customerId;
    private String portfolioId;
    private PortfolioManagementType managementType;
    private String reason;
    private Object additionalParams;
}