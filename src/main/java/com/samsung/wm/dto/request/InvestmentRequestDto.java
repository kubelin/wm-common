package com.samsung.wm.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 투자 요청 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvestmentRequestDto {
    
    @NotBlank(message = "고객 ID는 필수입니다")
    private String customerId;
    
    @NotNull(message = "투자 금액은 필수입니다")
    @DecimalMin(value = "10000", message = "최소 투자 금액은 1만원입니다")
    private BigDecimal investmentAmount;
    
    @NotBlank(message = "위험 성향은 필수입니다")
    private String riskProfile;
    
    @NotBlank(message = "투자 기간은 필수입니다")
    private String investmentPeriod;
    
    @NotBlank(message = "투자 목표는 필수입니다")
    private String investmentGoal;
    
    private boolean hasExistingPortfolio = false;
}