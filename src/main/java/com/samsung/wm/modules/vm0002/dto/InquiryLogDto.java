package com.samsung.wm.modules.vm0002.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * VM0002 잔고 조회 로그 DTO
 * vm0002.h의 inquiry_log_t 구조체를 Java로 변환
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InquiryLogDto {
    
    private Long logId;
    private String customerId;
    private String inquiryType;     // BALANCE, DETAIL 등
    private LocalDateTime inquiryTime;
    private Integer accountCount;   // 조회된 계좌 수
    
}