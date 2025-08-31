package com.samsung.wm.strategy.consultation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 고객 상담 결과 클래스
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsultationResult {
    
    private String customerId;
    private ConsultationType type;
    private String summary;
    private Map<String, Object> details;
    private LocalDateTime consultedAt;
    private boolean success;
}