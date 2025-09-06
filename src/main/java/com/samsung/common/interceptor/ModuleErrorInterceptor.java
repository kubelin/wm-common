package com.samsung.common.interceptor;

import com.samsung.common.constants.ErrorCodes;
import com.samsung.common.exception.ModuleException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 모듈 실행 에러 인터셉터
 * 공통모듈 실행 중 발생하는 모든 에러를 캐치하고 분석
 * 
 * 주요 기능:
 * 1. 에러 자동 캐치 및 분류
 * 2. 에러 통계 수집
 * 3. 재시도 로직
 * 4. 에러 패턴 분석
 */
@Slf4j
@Component
public class ModuleErrorInterceptor {
    
    // 모듈별 에러 통계
    private final Map<String, ModuleErrorStats> moduleErrorStats = new ConcurrentHashMap<>();
    
    // 전체 에러 카운터
    private final AtomicLong totalErrorCount = new AtomicLong(0);
    private final AtomicLong totalRecoveredCount = new AtomicLong(0);
    
    /**
     * 모듈별 에러 통계 클래스
     */
    public static class ModuleErrorStats {
        private final AtomicInteger inputValidationErrors = new AtomicInteger(0);
        private final AtomicInteger businessLogicErrors = new AtomicInteger(0);
        private final AtomicInteger databaseErrors = new AtomicInteger(0);
        private final AtomicInteger externalApiErrors = new AtomicInteger(0);
        private final AtomicInteger systemErrors = new AtomicInteger(0);
        private final AtomicInteger recoveredErrors = new AtomicInteger(0);
        private final AtomicInteger criticalErrors = new AtomicInteger(0);
        
        // Getters
        public int getInputValidationErrors() { return inputValidationErrors.get(); }
        public int getBusinessLogicErrors() { return businessLogicErrors.get(); }
        public int getDatabaseErrors() { return databaseErrors.get(); }
        public int getExternalApiErrors() { return externalApiErrors.get(); }
        public int getSystemErrors() { return systemErrors.get(); }
        public int getRecoveredErrors() { return recoveredErrors.get(); }
        public int getCriticalErrors() { return criticalErrors.get(); }
        public int getTotalErrors() { 
            return inputValidationErrors.get() + businessLogicErrors.get() + 
                   databaseErrors.get() + externalApiErrors.get() + systemErrors.get(); 
        }
    }
    
    /**
     * 모듈 실행을 감싸서 에러를 캐치하는 메서드
     */
    public <T> T executeWithErrorHandling(String moduleId, String operation, 
                                         ModuleOperation<T> moduleOperation, 
                                         Object inputData) {
        long startTime = System.currentTimeMillis();
        
        try {
            log.debug("[ERROR-INTERCEPTOR] 모듈 실행 시작: {} - {}", moduleId, operation);
            
            // 실제 모듈 로직 실행
            T result = moduleOperation.execute();
            
            long executionTime = System.currentTimeMillis() - startTime;
            log.debug("[ERROR-INTERCEPTOR] 모듈 실행 성공: {} - {}ms", moduleId, executionTime);
            
            return result;
            
        } catch (ModuleException e) {
            // ModuleException은 이미 분류된 에러이므로 그대로 처리
            handleModuleException(moduleId, e, startTime);
            throw e;
            
        } catch (IllegalArgumentException e) {
            // 입력 검증 에러로 분류
            ModuleException moduleEx = ModuleException.inputValidationError(
                moduleId, e.getMessage(), inputData);
            handleModuleException(moduleId, moduleEx, startTime);
            throw moduleEx;
            
        } catch (Exception e) {
            // 일반 예외를 시스템 에러로 분류
            ModuleException moduleEx = ModuleException.systemError(
                moduleId, "시스템 에러: " + e.getMessage(), e);
            handleModuleException(moduleId, moduleEx, startTime);
            throw moduleEx;
        }
    }
    
    /**
     * 재시도 로직이 포함된 실행 메서드
     */
    public <T> T executeWithRetry(String moduleId, String operation, 
                                  ModuleOperation<T> moduleOperation, 
                                  Object inputData, int maxRetries) {
        int retryCount = 0;
        ModuleException lastException = null;
        
        while (retryCount <= maxRetries) {
            try {
                if (retryCount > 0) {
                    log.info("[ERROR-INTERCEPTOR] 재시도 실행: {} - 시도 {}/{}", 
                            moduleId, retryCount, maxRetries);
                    
                    // 재시도 전 대기 (백오프 전략)
                    Thread.sleep(Math.min(1000 * retryCount, 5000));
                }
                
                return executeWithErrorHandling(moduleId, operation, moduleOperation, inputData);
                
            } catch (ModuleException e) {
                lastException = e;
                
                if (!e.isRetryable() || retryCount >= maxRetries) {
                    log.error("[ERROR-INTERCEPTOR] 재시도 중단: {} - {}", moduleId, e.getMessage());
                    break;
                }
                
                log.warn("[ERROR-INTERCEPTOR] 재시도 가능한 에러: {} - 재시도 중... ({}/{})", 
                        moduleId, retryCount + 1, maxRetries);
                retryCount++;
                
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                throw ModuleException.systemError(moduleId, "재시도 중 인터럽트 발생", ie);
            }
        }
        
        throw lastException;
    }
    
    /**
     * ModuleException 처리 및 통계 업데이트
     */
    private void handleModuleException(String moduleId, ModuleException exception, long startTime) {
        long executionTime = System.currentTimeMillis() - startTime;
        
        // 전체 통계 업데이트
        totalErrorCount.incrementAndGet();
        
        // 모듈별 통계 업데이트
        ModuleErrorStats stats = moduleErrorStats.computeIfAbsent(moduleId, k -> new ModuleErrorStats());
        updateErrorStats(stats, exception);
        
        // 로그 레벨 결정
        logError(moduleId, exception, executionTime);
        
        // 심각한 에러인 경우 알림
        if (exception.requiresImmediateAlert()) {
            triggerImmediateAlert(moduleId, exception);
        }
    }
    
    /**
     * 에러 통계 업데이트
     */
    private void updateErrorStats(ModuleErrorStats stats, ModuleException exception) {
        switch (exception.getErrorPhase()) {
            case "INPUT_VALIDATION":
                stats.inputValidationErrors.incrementAndGet();
                break;
            case "BUSINESS_LOGIC":
                stats.businessLogicErrors.incrementAndGet();
                break;
            case "DATABASE_ACCESS":
                stats.databaseErrors.incrementAndGet();
                break;
            case "EXTERNAL_API":
                stats.externalApiErrors.incrementAndGet();
                break;
            default:
                stats.systemErrors.incrementAndGet();
                break;
        }
        
        if (exception.getSeverity() == ModuleException.ErrorSeverity.CRITICAL) {
            stats.criticalErrors.incrementAndGet();
        }
    }
    
    /**
     * 에러 로깅
     */
    private void logError(String moduleId, ModuleException exception, long executionTime) {
        String logMessage = String.format(
            "[ERROR-INTERCEPTOR] 모듈 에러: %s | 코드: %s | 단계: %s | 심각도: %s | 복구가능: %s | 시간: %dms | 메시지: %s",
            moduleId, exception.getErrorCode(), exception.getErrorPhase(), 
            exception.getSeverity(), exception.isRecoverable(), executionTime, exception.getMessage()
        );
        
        switch (exception.getSeverity()) {
            case INFO:
                log.info(logMessage);
                break;
            case WARN:
                log.warn(logMessage);
                break;
            case ERROR:
                log.error(logMessage, exception);
                break;
            case CRITICAL:
                log.error("🚨 " + logMessage, exception);
                break;
        }
    }
    
    /**
     * 즉시 알림 트리거
     */
    private void triggerImmediateAlert(String moduleId, ModuleException exception) {
        // 실제 환경에서는 여기서 알림 시스템(슬랙, 이메일 등)으로 전송
        log.error("🚨🚨🚨 [IMMEDIATE-ALERT] 심각한 에러 발생 - 모듈: {}, 에러: {}", 
                 moduleId, exception.toString());
        
        // TODO: 실제 알림 시스템 연동
        // - Slack 알림
        // - 이메일 알림
        // - SMS 알림 (심각도에 따라)
        // - 모니터링 대시보드 업데이트
    }
    
    /**
     * 모듈별 에러 통계 조회
     */
    public ModuleErrorStats getModuleErrorStats(String moduleId) {
        return moduleErrorStats.getOrDefault(moduleId, new ModuleErrorStats());
    }
    
    /**
     * 전체 에러 통계 조회
     */
    public Map<String, Object> getOverallErrorStats() {
        return Map.of(
            "totalErrors", totalErrorCount.get(),
            "totalRecovered", totalRecoveredCount.get(),
            "moduleCount", moduleErrorStats.size(),
            "moduleStats", moduleErrorStats
        );
    }
    
    /**
     * 에러 통계 초기화 (테스트/관리용)
     */
    public void resetErrorStats() {
        moduleErrorStats.clear();
        totalErrorCount.set(0);
        totalRecoveredCount.set(0);
        log.info("[ERROR-INTERCEPTOR] 에러 통계 초기화 완료");
    }
    
    /**
     * 모듈 오퍼레이션 함수형 인터페이스
     */
    @FunctionalInterface
    public interface ModuleOperation<T> {
        T execute() throws Exception;
    }
}