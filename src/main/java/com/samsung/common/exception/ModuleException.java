package com.samsung.common.exception;

import com.samsung.common.constants.ErrorCodes;
import lombok.Getter;

/**
 * 공통모듈 전용 예외 클래스
 * C 모듈 에러를 정확히 캐치하고 분류하기 위한 예외
 */
@Getter
public class ModuleException extends RuntimeException {
    
    /**
     * 에러 코드 (ErrorCodes 상수 사용)
     */
    private final String errorCode;
    
    /**
     * 모듈 ID (vm0001, vm0002 등)
     */
    private final String moduleId;
    
    /**
     * 에러 발생 단계 (INPUT_VALIDATION, BUSINESS_LOGIC, DATABASE 등)
     */
    private final String errorPhase;
    
    /**
     * 원본 입력 데이터 (디버깅용)
     */
    private final Object inputData;
    
    /**
     * 복구 가능 여부
     */
    private final boolean recoverable;
    
    /**
     * 에러 심각도 (INFO, WARN, ERROR, CRITICAL)
     */
    private final ErrorSeverity severity;
    
    public enum ErrorSeverity {
        INFO,       // 정보성 (로그만 기록)
        WARN,       // 경고 (모니터링 필요)
        ERROR,      // 에러 (즉시 알림)
        CRITICAL    // 심각 (긴급 대응)
    }
    
    public enum ErrorPhase {
        INPUT_VALIDATION,    // 입력 검증 단계
        BUSINESS_LOGIC,      // 비즈니스 로직 단계
        DATABASE_ACCESS,     // 데이터베이스 접근 단계
        EXTERNAL_API,        // 외부 API 호출 단계
        OUTPUT_PROCESSING,   // 출력 처리 단계
        SYSTEM_INTERNAL     // 시스템 내부 단계
    }
    
    /**
     * 기본 생성자
     */
    public ModuleException(String errorCode, String moduleId, String message) {
        super(message);
        this.errorCode = errorCode;
        this.moduleId = moduleId;
        this.errorPhase = ErrorPhase.SYSTEM_INTERNAL.name();
        this.inputData = null;
        this.recoverable = false;
        this.severity = ErrorSeverity.ERROR;
    }
    
    /**
     * 상세 생성자
     */
    public ModuleException(String errorCode, String moduleId, String message, 
                          ErrorPhase errorPhase, Object inputData, 
                          boolean recoverable, ErrorSeverity severity) {
        super(message);
        this.errorCode = errorCode;
        this.moduleId = moduleId;
        this.errorPhase = errorPhase.name();
        this.inputData = inputData;
        this.recoverable = recoverable;
        this.severity = severity;
    }
    
    /**
     * 원인 예외가 있는 생성자
     */
    public ModuleException(String errorCode, String moduleId, String message, 
                          Throwable cause, ErrorPhase errorPhase, Object inputData,
                          boolean recoverable, ErrorSeverity severity) {
        super(message, cause);
        this.errorCode = errorCode;
        this.moduleId = moduleId;
        this.errorPhase = errorPhase.name();
        this.inputData = inputData;
        this.recoverable = recoverable;
        this.severity = severity;
    }
    
    /**
     * 입력 검증 에러 팩토리 메서드
     */
    public static ModuleException inputValidationError(String moduleId, String message, Object inputData) {
        return new ModuleException(
            ErrorCodes.INVALID_PARAMETER,
            moduleId,
            message,
            ErrorPhase.INPUT_VALIDATION,
            inputData,
            true,  // 입력 수정으로 복구 가능
            ErrorSeverity.WARN
        );
    }
    
    /**
     * 비즈니스 로직 에러 팩토리 메서드
     */
    public static ModuleException businessLogicError(String moduleId, String errorCode, String message, Object inputData) {
        return new ModuleException(
            errorCode,
            moduleId,
            message,
            ErrorPhase.BUSINESS_LOGIC,
            inputData,
            false,  // 비즈니스 규칙 위반은 일반적으로 복구 어려움
            ErrorSeverity.ERROR
        );
    }
    
    /**
     * 데이터베이스 에러 팩토리 메서드
     */
    public static ModuleException databaseError(String moduleId, String message, Throwable cause, Object inputData) {
        return new ModuleException(
            ErrorCodes.DATABASE_ERROR,
            moduleId,
            message,
            cause,
            ErrorPhase.DATABASE_ACCESS,
            inputData,
            true,  // 재시도 가능
            ErrorSeverity.CRITICAL
        );
    }
    
    /**
     * 외부 API 에러 팩토리 메서드
     */
    public static ModuleException externalApiError(String moduleId, String message, Throwable cause, Object inputData) {
        return new ModuleException(
            ErrorCodes.EXTERNAL_API_ERROR,
            moduleId,
            message,
            cause,
            ErrorPhase.EXTERNAL_API,
            inputData,
            true,  // 재시도 가능
            ErrorSeverity.ERROR
        );
    }
    
    /**
     * 시스템 에러 팩토리 메서드
     */
    public static ModuleException systemError(String moduleId, String message, Throwable cause) {
        return new ModuleException(
            ErrorCodes.UNKNOWN_ERROR,
            moduleId,
            message,
            cause,
            ErrorPhase.SYSTEM_INTERNAL,
            null,
            false,  // 시스템 에러는 일반적으로 복구 어려움
            ErrorSeverity.CRITICAL
        );
    }
    
    /**
     * 에러 정보 요약
     */
    @Override
    public String toString() {
        return String.format("ModuleException{errorCode='%s', moduleId='%s', phase='%s', severity='%s', recoverable=%s, message='%s'}",
                errorCode, moduleId, errorPhase, severity, recoverable, getMessage());
    }
    
    /**
     * 에러가 재시도 가능한지 판단
     */
    public boolean isRetryable() {
        return recoverable && (
            ErrorCodes.DATABASE_ERROR.equals(errorCode) ||
            ErrorCodes.EXTERNAL_API_ERROR.equals(errorCode) ||
            ErrorCodes.TIMEOUT_ERROR.equals(errorCode) ||
            ErrorCodes.NETWORK_ERROR.equals(errorCode)
        );
    }
    
    /**
     * 에러가 즉시 알림이 필요한지 판단
     */
    public boolean requiresImmediateAlert() {
        return severity == ErrorSeverity.CRITICAL || 
               (severity == ErrorSeverity.ERROR && !recoverable);
    }
}