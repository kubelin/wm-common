package com.samsung.wm.modules.vm0001.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * VM0001 고객정보 조회 출력 DTO  
 * C 파일의 출력 결과를 Java DTO로 변환
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Vm0001OutputDto {
    
    private String resultCode;
    private String message;
    private CustomerDto customerInfo;
    private LocalDateTime accessTime;
    
}