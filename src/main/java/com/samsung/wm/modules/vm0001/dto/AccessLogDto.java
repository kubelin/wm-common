package com.samsung.wm.modules.vm0001.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * VM0001 접근 로그 DTO
 * vm0001.h의 access_log_t 구조체를 Java로 변환
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccessLogDto {
    
    private Long logId;
    private String customerId;
    private String accessType;      // INQUIRY, UPDATE, etc.
    private LocalDateTime accessTime;
    
}