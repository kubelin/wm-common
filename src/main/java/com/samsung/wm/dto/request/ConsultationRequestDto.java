package com.samsung.wm.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Map;

/**
 * 고객 상담 요청 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsultationRequestDto {
    
    @NotBlank(message = "고객 ID는 필수입니다")
    private String customerId;
    
    @NotBlank(message = "상담 유형은 필수입니다")
    private String consultationType;
    
    private String consultationReason;
    
    private Map<String, Object> additionalInfo;
}