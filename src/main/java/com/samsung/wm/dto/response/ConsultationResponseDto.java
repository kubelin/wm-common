package com.samsung.wm.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 고객 상담 응답 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsultationResponseDto {
    
    private String customerId;
    private String consultationType;
    private String consultationTypeName;
    private String summary;
    private Map<String, Object> details;
    private LocalDateTime consultedAt;
    private boolean success;
    private String message;
}