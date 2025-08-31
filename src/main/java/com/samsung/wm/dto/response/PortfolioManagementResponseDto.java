package com.samsung.wm.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 포트폴리오 관리 응답 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioManagementResponseDto {
    
    private String portfolioId;
    private String customerId;
    private String managementType;
    private String managementTypeName;
    private String summary;
    private List<String> actions;
    private Map<String, Object> details;
    private LocalDateTime executedAt;
    private boolean success;
    private String message;
}