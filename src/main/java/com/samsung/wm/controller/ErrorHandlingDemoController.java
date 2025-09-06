package com.samsung.wm.controller;

import com.samsung.common.interceptor.ModuleErrorInterceptor;
import com.samsung.common.monitor.ModuleErrorMonitor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 에러 처리 시스템 데모 컨트롤러
 * 공통모듈 에러 캐치 시스템 테스트 및 모니터링
 */
@Slf4j
@RestController
@RequestMapping("/api/error-demo")
@RequiredArgsConstructor
public class ErrorHandlingDemoController {
    
    private final ModuleErrorInterceptor errorInterceptor;
    private final ModuleErrorMonitor errorMonitor;
    
    /**
     * 전체 에러 통계 조회
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getErrorStats() {
        log.info("에러 통계 조회 요청");
        
        Map<String, Object> stats = errorInterceptor.getOverallErrorStats();
        return ResponseEntity.ok(stats);
    }
    
    /**
     * 특정 모듈의 에러 통계 조회
     */
    @GetMapping("/stats/{moduleId}")
    public ResponseEntity<Map<String, Object>> getModuleErrorStats(@PathVariable String moduleId) {
        log.info("모듈 {} 에러 통계 조회 요청", moduleId);
        
        ModuleErrorInterceptor.ModuleErrorStats stats = errorInterceptor.getModuleErrorStats(moduleId);
        
        Map<String, Object> result = Map.of(
            "moduleId", moduleId,
            "totalErrors", stats.getTotalErrors(),
            "inputValidationErrors", stats.getInputValidationErrors(),
            "businessLogicErrors", stats.getBusinessLogicErrors(),
            "databaseErrors", stats.getDatabaseErrors(),
            "externalApiErrors", stats.getExternalApiErrors(),
            "systemErrors", stats.getSystemErrors(),
            "recoveredErrors", stats.getRecoveredErrors(),
            "criticalErrors", stats.getCriticalErrors()
        );
        
        return ResponseEntity.ok(result);
    }
    
    /**
     * 특정 모듈의 상세 에러 분석 실행
     */
    @PostMapping("/analyze/{moduleId}")
    public ResponseEntity<Map<String, String>> analyzeModuleErrors(@PathVariable String moduleId) {
        log.info("모듈 {} 상세 분석 요청", moduleId);
        
        try {
            errorMonitor.analyzeModuleErrors(moduleId);
            return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "모듈 " + moduleId + " 분석 완료. 로그를 확인하세요."
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "status", "error",
                "message", "분석 중 오류 발생: " + e.getMessage()
            ));
        }
    }
    
    /**
     * 에러 통계 수동 리포트 생성
     */
    @PostMapping("/report")
    public ResponseEntity<Map<String, String>> generateErrorReport() {
        log.info("수동 에러 리포트 생성 요청");
        
        try {
            // ModuleErrorMonitor의 generateErrorReport()는 package-private이므로
            // 여기서는 간단한 통계만 제공
            Map<String, Object> stats = errorInterceptor.getOverallErrorStats();
            
            long totalErrors = (Long) stats.get("totalErrors");
            long totalRecovered = (Long) stats.get("totalRecovered");
            int moduleCount = (Integer) stats.get("moduleCount");
            
            String reportMessage = String.format(
                "📊 현재 에러 통계: 총 %d건, 복구 %d건, 활성 모듈 %d개. 상세한 내용은 로그를 확인하세요.",
                totalErrors, totalRecovered, moduleCount
            );
            
            return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", reportMessage,
                "totalErrors", String.valueOf(totalErrors),
                "totalRecovered", String.valueOf(totalRecovered),
                "activeModules", String.valueOf(moduleCount)
            ));
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "status", "error", 
                "message", "리포트 생성 중 오류 발생: " + e.getMessage()
            ));
        }
    }
    
    /**
     * 에러 통계 초기화 (테스트용)
     */
    @DeleteMapping("/stats")
    public ResponseEntity<Map<String, String>> resetErrorStats() {
        log.info("에러 통계 초기화 요청");
        
        try {
            errorInterceptor.resetErrorStats();
            return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "에러 통계가 초기화되었습니다."
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "status", "error",
                "message", "통계 초기화 중 오류 발생: " + e.getMessage()
            ));
        }
    }
    
    /**
     * 에러 캐치 시스템 상태 확인
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> checkErrorSystemHealth() {
        log.info("에러 시스템 상태 확인 요청");
        
        Map<String, Object> health = Map.of(
            "errorInterceptor", errorInterceptor != null ? "available" : "unavailable",
            "errorMonitor", errorMonitor != null ? "available" : "unavailable", 
            "features", Map.of(
                "errorCatching", "enabled",
                "errorStatistics", "enabled", 
                "errorMonitoring", "enabled",
                "retryLogic", "enabled",
                "alerting", "enabled"
            ),
            "errorCodes", Map.of(
                "totalDefinitions", "100+",
                "categories", "공통, 인증, 비즈니스, 상담, 투자, 포트폴리오, 고객, 시스템, 검증, 파일"
            )
        );
        
        return ResponseEntity.ok(health);
    }
    
    /**
     * 사용 가능한 API 엔드포인트 목록
     */
    @GetMapping("/endpoints")
    public ResponseEntity<Map<String, Object>> getAvailableEndpoints() {
        Map<String, Object> endpoints = Map.of(
            "statistics", Map.of(
                "GET /api/error-demo/stats", "전체 에러 통계 조회",
                "GET /api/error-demo/stats/{moduleId}", "모듈별 에러 통계 조회"
            ),
            "analysis", Map.of(
                "POST /api/error-demo/analyze/{moduleId}", "모듈 에러 상세 분석",
                "POST /api/error-demo/report", "에러 리포트 수동 생성"
            ),
            "management", Map.of(
                "DELETE /api/error-demo/stats", "에러 통계 초기화 (테스트용)",
                "GET /api/error-demo/health", "에러 시스템 상태 확인"
            ),
            "testing", Map.of(
                "POST /api/module/vm0001", "vm0001 모듈 테스트 (에러 발생 가능)",
                "POST /api/module/vm0002", "vm0002 모듈 테스트 (에러 발생 가능)",
                "POST /api/module/invalid", "존재하지 않는 모듈 테스트 (DefaultService 사용)"
            )
        );
        
        return ResponseEntity.ok(endpoints);
    }
}