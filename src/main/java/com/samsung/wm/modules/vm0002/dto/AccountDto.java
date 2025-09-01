package com.samsung.wm.modules.vm0002.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * VM0002 계좌 정보 DTO
 * vm0002.h의 account_info_t 구조체를 Java로 변환
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {
    
    private String accountNo;       // 계좌번호
    private String customerId;      // 고객ID
    private String accountType;     // 계좌유형 (SAVINGS, CHECKING, INVESTMENT)
    private BigDecimal balance;     // 잔고
    private LocalDateTime lastTransactionTime; // 마지막 거래 시간
    private Double interestRate;    // 이자율
    
}