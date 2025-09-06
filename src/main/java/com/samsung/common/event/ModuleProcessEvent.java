package com.samsung.common.event;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 모듈 처리 이벤트
 * C 함수의 깊은 호출 구조를 Event-Driven으로 단순화
 */
@Getter
@Builder
@ToString
public class ModuleProcessEvent {
    
    /**
     * 이벤트 타입 (C 함수 호출 단계별)
     */
    public enum EventType {
        // 모듈 처리 시작
        MODULE_STARTED,
        
        // 입력 검증 완료
        INPUT_VALIDATED,
        
        // 고객 정보 검증 완료
        CUSTOMER_VALIDATED,
        
        // 비즈니스 로직 처리 완료
        BUSINESS_PROCESSED,
        
        // 데이터베이스 처리 완료
        DATABASE_PROCESSED,
        
        // 계산/분석 완료
        CALCULATION_COMPLETED,
        
        // 로그 기록 완료
        LOG_RECORDED,
        
        // 알림 발송 완료
        NOTIFICATION_SENT,
        
        // 모듈 처리 완전 완료
        MODULE_COMPLETED,
        
        // 에러 발생
        MODULE_ERROR,
        
        // 체인 처리 완료 (여러 모듈 연계)
        CHAIN_COMPLETED
    }
    
    /**
     * 이벤트 타입
     */
    private final EventType eventType;
    
    /**
     * 서비스 ID (vm0001, vm0002 등)
     */
    private final String serviceId;
    
    /**
     * 처리 결과 데이터 (타입 안전하지 않음 - 범용성을 위해)
     */
    private final Object resultData;
    
    /**
     * 처리 메타데이터
     */
    private final Map<String, Object> metadata;
    
    /**
     * 에러 정보 (에러 발생 시)
     */
    private final Exception error;
    
    /**
     * 처리 시간
     */
    private final LocalDateTime timestamp;
    
    /**
     * 실행 시간 (milliseconds)
     */
    private final Long executionTime;
    
    /**
     * 체인 ID (여러 모듈 연계 처리 시)
     */
    private final String chainId;
    
    /**
     * 이전 단계 결과 (체인 처리에서 사용)
     */
    private final Object previousResult;
}