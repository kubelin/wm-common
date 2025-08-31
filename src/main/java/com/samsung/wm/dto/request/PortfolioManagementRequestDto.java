package com.samsung.wm.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;

/**
 * 포트폴리오 관리 요청 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioManagementRequestDto {
    
    @NotBlank(message = "고객 ID는 필수입니다")
    private String customerId;
    
    @NotBlank(message = "포트폴리오 ID는 필수입니다")
    private String portfolioId;
    
    @NotBlank(message = "관리 유형은 필수입니다")
    private String managementType;
    
    private String reason;
    
    private Object additionalParams;
}