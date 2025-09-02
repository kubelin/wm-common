package com.samsung.wm.modules.vm0002.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * VM0002 계좌잔고 조회 출력 DTO
 * C 파일의 출력 결과를 Java DTO로 변환
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Vm0002OutputDto {
    
    private String resultCode;
    private String message;
    private String customerId;
    private Integer accountCount;
    private BigDecimal totalBalance;
    private List<AccountDto> accounts;
    private LocalDateTime inquiryTime;
    
}