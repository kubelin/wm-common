package com.samsung.wm.modules.vm0001.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * VM0001 고객 정보 DTO
 * vm0001.h의 customer_info_t 구조체를 Java로 변환
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {
    
    private String customerId;      // CUST001 형태
    private String customerName;    // 고객명
    private String status;          // 상태 (ACTIVE, INACTIVE)
    private String createTime;      // 생성일시
    private LocalDateTime lastAccessTime; // 마지막 접근 시간
    
}