package com.samsung.wm.modules.vm0002.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * VM0002 계좌잔고 조회 입력 DTO
 * C 파일의 입력 파라미터를 Java DTO로 변환
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Vm0002InputDto {
    
    @NotBlank(message = "고객ID는 필수입니다")
    @Size(min = 7, max = 7, message = "고객ID는 7자리여야 합니다")
    private String customerId;
    
    private String accountType; // 선택사항 (SAVINGS, CHECKING, INVESTMENT)
    
}