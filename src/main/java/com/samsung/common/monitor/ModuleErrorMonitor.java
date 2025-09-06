package com.samsung.common.monitor;

import com.samsung.common.interceptor.ModuleErrorInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * 모듈 에러 모니터링 시스템
 * 공통모듈의 에러 패턴을 분석하고 알림을 제공
 * 
 * 주요 기능:
 * 1. 주기적 에러 통계 리포팅
 * 2. 에러 패턴 분석
 * 3. 임계치 기반 알림
 * 4. 에러 트렌드 분석
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ModuleErrorMonitor {
    
    private final ModuleErrorInterceptor errorInterceptor;
    
    // 에러 임계치 설정
    private static final int CRITICAL_ERROR_THRESHOLD = 10;  // 심각 에러 10개 이상
    private static final int TOTAL_ERROR_THRESHOLD = 50;     // 전체 에러 50개 이상
    private static final double ERROR_RATE_THRESHOLD = 0.1;  // 에러율 10% 이상
    
    /**
     * 5분마다 에러 통계 리포트 생성
     */
    @Scheduled(fixedRate = 300000) // 5분 = 300,000ms
    public void generateErrorReport() {
        try {
            Map<String, Object> overallStats = errorInterceptor.getOverallErrorStats();
            
            long totalErrors = (Long) overallStats.get("totalErrors");
            long totalRecovered = (Long) overallStats.get("totalRecovered");
            int moduleCount = (Integer) overallStats.get("moduleCount");
            
            if (totalErrors == 0) {
                log.info("📊 [ERROR-MONITOR] 에러 없음 - 모든 모듈 정상 작동 중");
                return;
            }
            
            // 전체 통계 로그
            String reportTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            log.info("📊 [ERROR-MONITOR] 에러 통계 리포트 - {}", reportTime);
            log.info("   총 에러 수: {} | 복구된 에러: {} | 활성 모듈: {}", 
                    totalErrors, totalRecovered, moduleCount);
            
            // 모듈별 상세 통계
            @SuppressWarnings("unchecked")
            Map<String, ModuleErrorInterceptor.ModuleErrorStats> moduleStats = 
                (Map<String, ModuleErrorInterceptor.ModuleErrorStats>) overallStats.get("moduleStats");
            
            moduleStats.forEach(this::reportModuleStats);
            
            // 임계치 체크 및 알림
            checkErrorThresholds(totalErrors, moduleStats);
            
        } catch (Exception e) {
            log.error("[ERROR-MONITOR] 에러 리포트 생성 중 오류 발생", e);
        }
    }
    
    /**
     * 모듈별 에러 통계 리포트
     */
    private void reportModuleStats(String moduleId, ModuleErrorInterceptor.ModuleErrorStats stats) {
        int totalErrors = stats.getTotalErrors();
        
        if (totalErrors == 0) {
            log.debug("   ✅ {} - 에러 없음", moduleId);
            return;
        }
        
        StringBuilder report = new StringBuilder();
        report.append(String.format("   📋 %s - 총: %d", moduleId, totalErrors));
        
        if (stats.getInputValidationErrors() > 0) {
            report.append(String.format(" | 입력검증: %d", stats.getInputValidationErrors()));
        }
        if (stats.getBusinessLogicErrors() > 0) {
            report.append(String.format(" | 비즈니스: %d", stats.getBusinessLogicErrors()));
        }
        if (stats.getDatabaseErrors() > 0) {
            report.append(String.format(" | DB: %d", stats.getDatabaseErrors()));
        }
        if (stats.getExternalApiErrors() > 0) {
            report.append(String.format(" | 외부API: %d", stats.getExternalApiErrors()));
        }
        if (stats.getSystemErrors() > 0) {
            report.append(String.format(" | 시스템: %d", stats.getSystemErrors()));
        }
        if (stats.getCriticalErrors() > 0) {
            report.append(String.format(" | 🚨심각: %d", stats.getCriticalErrors()));
        }
        if (stats.getRecoveredErrors() > 0) {
            report.append(String.format(" | ✅복구: %d", stats.getRecoveredErrors()));
        }
        
        log.info(report.toString());
    }
    
    /**
     * 에러 임계치 체크 및 알림
     */
    private void checkErrorThresholds(long totalErrors, 
                                    Map<String, ModuleErrorInterceptor.ModuleErrorStats> moduleStats) {
        
        // 전체 에러 수 임계치 체크
        if (totalErrors >= TOTAL_ERROR_THRESHOLD) {
            sendAlert("전체 에러 수 임계치 초과", 
                     String.format("전체 에러 수: %d (임계치: %d)", totalErrors, TOTAL_ERROR_THRESHOLD));
        }
        
        // 모듈별 심각한 에러 체크
        moduleStats.forEach((moduleId, stats) -> {
            if (stats.getCriticalErrors() >= CRITICAL_ERROR_THRESHOLD) {
                sendAlert("심각 에러 임계치 초과", 
                         String.format("모듈 %s - 심각 에러: %d (임계치: %d)", 
                                     moduleId, stats.getCriticalErrors(), CRITICAL_ERROR_THRESHOLD));
            }
            
            // 특정 모듈의 에러율이 높은 경우
            int totalModuleErrors = stats.getTotalErrors();
            if (totalModuleErrors >= 20) {  // 최소 20개 에러가 있어야 의미있는 통계
                double errorRate = (double) totalModuleErrors / (totalModuleErrors + stats.getRecoveredErrors());
                if (errorRate >= ERROR_RATE_THRESHOLD) {
                    sendAlert("모듈 에러율 임계치 초과", 
                             String.format("모듈 %s - 에러율: %.1f%% (임계치: %.1f%%)", 
                                         moduleId, errorRate * 100, ERROR_RATE_THRESHOLD * 100));
                }
            }
        });
    }
    
    /**
     * 매시간 에러 트렌드 분석
     */
    @Scheduled(fixedRate = 3600000) // 1시간 = 3,600,000ms
    public void analyzeErrorTrends() {
        try {
            log.info("📈 [ERROR-MONITOR] 에러 트렌드 분석 시작");
            
            Map<String, Object> overallStats = errorInterceptor.getOverallErrorStats();
            
            // TODO: 시간별 에러 데이터 수집 및 트렌드 분석
            // 실제 구현 시에는 데이터베이스에 시간별 통계를 저장하고
            // 트렌드 분석을 수행해야 함
            
            @SuppressWarnings("unchecked")
            Map<String, ModuleErrorInterceptor.ModuleErrorStats> moduleStats = 
                (Map<String, ModuleErrorInterceptor.ModuleErrorStats>) overallStats.get("moduleStats");
            
            // 에러가 가장 많은 모듈 TOP 3 분석
            moduleStats.entrySet().stream()
                .filter(entry -> entry.getValue().getTotalErrors() > 0)
                .sorted((e1, e2) -> Integer.compare(
                    e2.getValue().getTotalErrors(), 
                    e1.getValue().getTotalErrors()))
                .limit(3)
                .forEach(entry -> {
                    String moduleId = entry.getKey();
                    ModuleErrorInterceptor.ModuleErrorStats stats = entry.getValue();
                    log.info("   🔥 TOP 에러 모듈: {} - 총 {}건", moduleId, stats.getTotalErrors());
                });
            
            log.info("📈 [ERROR-MONITOR] 에러 트렌드 분석 완료");
            
        } catch (Exception e) {
            log.error("[ERROR-MONITOR] 에러 트렌드 분석 중 오류 발생", e);
        }
    }
    
    /**
     * 매일 자정 에러 통계 초기화 (선택적)
     */
    @Scheduled(cron = "0 0 0 * * *") // 매일 자정
    public void dailyStatsReset() {
        try {
            log.info("🔄 [ERROR-MONITOR] 일일 통계 초기화");
            
            // 실제 운영 환경에서는 통계를 초기화하기 전에 
            // 데이터베이스나 파일에 백업해야 함
            
            Map<String, Object> finalStats = errorInterceptor.getOverallErrorStats();
            log.info("📊 일일 최종 통계 - 총 에러: {}, 복구: {}", 
                    finalStats.get("totalErrors"), finalStats.get("totalRecovered"));
            
            // TODO: 실제 환경에서는 통계 백업 후 초기화
            // errorInterceptor.resetErrorStats();
            
        } catch (Exception e) {
            log.error("[ERROR-MONITOR] 일일 통계 초기화 중 오류 발생", e);
        }
    }
    
    /**
     * 특정 모듈의 상세 에러 분석
     */
    public void analyzeModuleErrors(String moduleId) {
        try {
            ModuleErrorInterceptor.ModuleErrorStats stats = errorInterceptor.getModuleErrorStats(moduleId);
            
            log.info("🔍 [ERROR-MONITOR] 모듈 {} 상세 분석:", moduleId);
            log.info("   총 에러: {}", stats.getTotalErrors());
            log.info("   입력 검증 에러: {} ({}%)", stats.getInputValidationErrors(), 
                    calculatePercentage(stats.getInputValidationErrors(), stats.getTotalErrors()));
            log.info("   비즈니스 로직 에러: {} ({}%)", stats.getBusinessLogicErrors(),
                    calculatePercentage(stats.getBusinessLogicErrors(), stats.getTotalErrors()));
            log.info("   데이터베이스 에러: {} ({}%)", stats.getDatabaseErrors(),
                    calculatePercentage(stats.getDatabaseErrors(), stats.getTotalErrors()));
            log.info("   외부 API 에러: {} ({}%)", stats.getExternalApiErrors(),
                    calculatePercentage(stats.getExternalApiErrors(), stats.getTotalErrors()));
            log.info("   시스템 에러: {} ({}%)", stats.getSystemErrors(),
                    calculatePercentage(stats.getSystemErrors(), stats.getTotalErrors()));
            log.info("   심각 에러: {} ({}%)", stats.getCriticalErrors(),
                    calculatePercentage(stats.getCriticalErrors(), stats.getTotalErrors()));
            log.info("   복구된 에러: {}", stats.getRecoveredErrors());
            
        } catch (Exception e) {
            log.error("[ERROR-MONITOR] 모듈 {} 분석 중 오류 발생", moduleId, e);
        }
    }
    
    /**
     * 에러 알림 전송
     */
    private void sendAlert(String alertType, String message) {
        String alertMessage = String.format("🚨 [ALERT] %s - %s", alertType, message);
        log.warn(alertMessage);
        
        // TODO: 실제 알림 시스템 연동
        // - Slack 웹훅 전송
        // - 이메일 발송
        // - SMS 발송 (심각도에 따라)
        // - 모니터링 대시보드 업데이트
        // - PagerDuty 등 인시던트 관리 도구 연동
    }
    
    /**
     * 백분율 계산 유틸리티
     */
    private String calculatePercentage(int value, int total) {
        if (total == 0) return "0";
        return String.format("%.1f", (value * 100.0) / total);
    }
}