package com.samsung.wm.strategy.portfolio;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 포트폴리오 관리 결과 클래스
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioManagementResult {
    
    private String portfolioId;
    private String customerId;
    private PortfolioManagementType type;
    private String summary;
    private List<String> actions;
    private Map<String, Object> details;
    private LocalDateTime executedAt;
    private boolean success;
}