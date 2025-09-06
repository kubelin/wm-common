package com.samsung.common.handler;

import com.samsung.common.constants.ErrorCodes;
import com.samsung.common.exception.ModuleException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 간단한 모듈 에러 핸들러
 * 공통모듈에서 발생한 에러를 적절히 분류하고 처리
 */
@Slf4j
@Component
public class ModuleErrorHandler {
    
    /**
     * 일반 Exception을 ModuleException으로 변환
     */
    public ModuleException handleException(String moduleId, Exception e, Object inputData) {
        if (e instanceof ModuleException) {
            // 이미 ModuleException인 경우 그대로 반환
            return (ModuleException) e;
        }
        
        if (e instanceof IllegalArgumentException) {
            // 입력 검증 에러
            log.warn("[ERROR-HANDLER] 입력 검증 에러 발생 - 모듈: {}, 메시지: {}", moduleId, e.getMessage());
            return ModuleException.inputValidationError(moduleId, e.getMessage(), inputData);
        }
        
        // 데이터베이스 관련 에러 체크
        if (isDatabaseError(e)) {
            log.error("[ERROR-HANDLER] 데이터베이스 에러 발생 - 모듈: {}, 메시지: {}", moduleId, e.getMessage());
            return ModuleException.databaseError(moduleId, e.getMessage(), e, inputData);
        }
        
        // 외부 API 에러 체크
        if (isExternalApiError(e)) {
            log.error("[ERROR-HANDLER] 외부 API 에러 발생 - 모듈: {}, 메시지: {}", moduleId, e.getMessage());
            return ModuleException.externalApiError(moduleId, e.getMessage(), e, inputData);
        }
        
        // 기본: 시스템 에러로 처리
        log.error("[ERROR-HANDLER] 시스템 에러 발생 - 모듈: {}, 메시지: {}", moduleId, e.getMessage());
        return ModuleException.systemError(moduleId, e.getMessage(), e);
    }
    
    /**
     * ModuleException을 적절한 에러코드로 변환
     */
    public String getErrorCode(ModuleException e) {
        // ModuleException에 이미 에러코드가 있으면 그대로 사용
        if (e.getErrorCode() != null) {
            return e.getErrorCode();
        }
        
        // 에러 단계에 따른 기본 에러코드 결정
        switch (e.getErrorPhase()) {
            case "INPUT_VALIDATION":
                return ErrorCodes.INVALID_PARAMETER;
            case "BUSINESS_LOGIC":
                return ErrorCodes.BUSINESS_RULE_VIOLATION;
            case "DATABASE_ACCESS":
                return ErrorCodes.DATABASE_ERROR;
            case "EXTERNAL_API":
                return ErrorCodes.EXTERNAL_API_ERROR;
            default:
                return ErrorCodes.UNKNOWN_ERROR;
        }
    }
    
    /**
     * 에러 심각도에 따른 로깅 레벨 결정
     */
    public void logError(String moduleId, ModuleException e) {
        String logMessage = String.format("[%s] 에러코드: %s, 단계: %s, 메시지: %s", 
                                         moduleId, e.getErrorCode(), e.getErrorPhase(), e.getMessage());
        
        switch (e.getSeverity()) {
            case INFO:
                log.info(logMessage);
                break;
            case WARN:
                log.warn(logMessage);
                break;
            case ERROR:
                log.error(logMessage, e);
                break;
            case CRITICAL:
                log.error("🚨 CRITICAL: " + logMessage, e);
                // 실제 환경에서는 여기서 긴급 알림 발송
                break;
        }
    }
    
    /**
     * 데이터베이스 에러인지 확인
     */
    private boolean isDatabaseError(Exception e) {
        String className = e.getClass().getName().toLowerCase();
        String message = e.getMessage() != null ? e.getMessage().toLowerCase() : "";
        
        return className.contains("sql") ||
               className.contains("database") ||
               className.contains("connection") ||
               message.contains("connection") ||
               message.contains("database") ||
               message.contains("sql");
    }
    
    /**
     * 외부 API 에러인지 확인
     */
    private boolean isExternalApiError(Exception e) {
        String className = e.getClass().getName().toLowerCase();
        String message = e.getMessage() != null ? e.getMessage().toLowerCase() : "";
        
        return className.contains("http") ||
               className.contains("rest") ||
               className.contains("client") ||
               message.contains("connection timeout") ||
               message.contains("api") ||
               message.contains("service unavailable");
    }
}