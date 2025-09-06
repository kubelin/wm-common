package com.samsung.common.orchestrator;

import com.samsung.common.event.ModuleEventPublisher;
import com.samsung.common.factory.ModuleServiceFactory;
import com.samsung.common.service.ModuleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 모듈 체인 오케스트레이터
 * C 함수의 깊은 호출 구조(20~30 depth)를 체인으로 처리
 * 
 * 사용 예시:
 * vm0001 (고객조회) → vm0002 (계좌조회) → vm0003 (위험도분석) → vm0004 (추천상품)
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ModuleChainOrchestrator {
    
    private final ModuleServiceFactory factory;
    private final ModuleEventPublisher eventPublisher;
    
    /**
     * 체인 단계 정의
     */
    public static class ChainStep {
        private final String serviceId;
        private final String description;
        private final boolean required;
        
        public ChainStep(String serviceId, String description, boolean required) {
            this.serviceId = serviceId;
            this.description = description;
            this.required = required;
        }
        
        public String getServiceId() { return serviceId; }
        public String getDescription() { return description; }
        public boolean isRequired() { return required; }
    }
    
    /**
     * 체인 처리 결과
     */
    public static class ChainResult {
        private final String chainId;
        private final boolean success;
        private final List<Object> stepResults;
        private final List<String> executedSteps;
        private final List<String> failedSteps;
        private final Long totalExecutionTime;
        private final LocalDateTime startTime;
        private final LocalDateTime endTime;
        
        public ChainResult(String chainId, boolean success, List<Object> stepResults, 
                          List<String> executedSteps, List<String> failedSteps,
                          Long totalExecutionTime, LocalDateTime startTime, LocalDateTime endTime) {
            this.chainId = chainId;
            this.success = success;
            this.stepResults = stepResults;
            this.executedSteps = executedSteps;
            this.failedSteps = failedSteps;
            this.totalExecutionTime = totalExecutionTime;
            this.startTime = startTime;
            this.endTime = endTime;
        }
        
        // Getters
        public String getChainId() { return chainId; }
        public boolean isSuccess() { return success; }
        public List<Object> getStepResults() { return stepResults; }
        public List<String> getExecutedSteps() { return executedSteps; }
        public List<String> getFailedSteps() { return failedSteps; }
        public Long getTotalExecutionTime() { return totalExecutionTime; }
        public LocalDateTime getStartTime() { return startTime; }
        public LocalDateTime getEndTime() { return endTime; }
    }
    
    /**
     * 순차적 체인 실행
     * 이전 단계의 결과가 다음 단계의 입력이 됨 (C 함수 호출과 유사)
     */
    public ChainResult executeSequentialChain(List<ChainStep> steps, Map<String, Object> initialInput) {
        String chainId = "SEQ-CHAIN-" + UUID.randomUUID().toString().substring(0, 8);
        LocalDateTime startTime = LocalDateTime.now();
        long totalStartTime = System.currentTimeMillis();
        
        List<Object> stepResults = new ArrayList<>();
        List<String> executedSteps = new ArrayList<>();
        List<String> failedSteps = new ArrayList<>();
        Map<String, Object> currentInput = initialInput;
        
        log.info("[CHAIN] 순차 체인 처리 시작 - chainId: {}, steps: {}", chainId, steps.size());
        
        try {
            for (int i = 0; i < steps.size(); i++) {
                ChainStep step = steps.get(i);
                String stepId = step.getServiceId();
                
                try {
                    log.info("[CHAIN] 단계 {}/{} 실행 중: {} ({})", 
                            i + 1, steps.size(), stepId, step.getDescription());
                    
                    // 모듈 서비스 실행
                    ModuleService service = factory.getService(stepId);
                    Object stepResult = service.process(currentInput, Object.class);
                    
                    // 결과 저장
                    stepResults.add(stepResult);
                    executedSteps.add(stepId);
                    
                    // 다음 단계의 입력으로 현재 결과 사용 (C 함수 체인과 동일)
                    if (stepResult instanceof Map) {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> resultMap = (Map<String, Object>) stepResult;
                        currentInput = resultMap;
                    } else {
                        // DTO인 경우 Map으로 변환
                        currentInput = convertToMap(stepResult);
                    }
                    
                    log.info("[CHAIN] 단계 {}/{} 완료: {}", i + 1, steps.size(), stepId);
                    
                } catch (Exception e) {
                    log.error("[CHAIN] 단계 실행 실패: {} - {}", stepId, e.getMessage());
                    failedSteps.add(stepId);
                    
                    if (step.isRequired()) {
                        log.error("[CHAIN] 필수 단계 실패로 체인 중단: {}", stepId);
                        break;
                    } else {
                        log.warn("[CHAIN] 선택적 단계 실패, 계속 진행: {}", stepId);
                        stepResults.add(null); // 실패한 단계는 null로 표시
                    }
                }
            }
            
            long totalExecutionTime = System.currentTimeMillis() - totalStartTime;
            LocalDateTime endTime = LocalDateTime.now();
            
            // 최종 결과 생성
            boolean success = failedSteps.isEmpty() || 
                             steps.stream().filter(step -> failedSteps.contains(step.getServiceId()))
                                  .noneMatch(ChainStep::isRequired);
            
            ChainResult result = new ChainResult(
                chainId, success, stepResults, executedSteps, failedSteps,
                totalExecutionTime, startTime, endTime
            );
            
            // Event: 체인 처리 완료
            eventPublisher.publishChainCompleted(chainId, result, totalExecutionTime);
            
            log.info("[CHAIN] 순차 체인 처리 완료 - chainId: {}, success: {}, 실행: {}, 실패: {}, 시간: {}ms", 
                    chainId, success, executedSteps.size(), failedSteps.size(), totalExecutionTime);
            
            return result;
            
        } catch (Exception e) {
            log.error("[CHAIN] 체인 처리 중 예외 발생: {}", e.getMessage(), e);
            
            long totalExecutionTime = System.currentTimeMillis() - totalStartTime;
            return new ChainResult(
                chainId, false, stepResults, executedSteps, failedSteps,
                totalExecutionTime, startTime, LocalDateTime.now()
            );
        }
    }
    
    /**
     * 병렬 체인 실행
     * 모든 단계를 동시에 실행 (독립적인 처리)
     */
    public ChainResult executeParallelChain(List<ChainStep> steps, Map<String, Object> sharedInput) {
        String chainId = "PAR-CHAIN-" + UUID.randomUUID().toString().substring(0, 8);
        LocalDateTime startTime = LocalDateTime.now();
        long totalStartTime = System.currentTimeMillis();
        
        List<Object> stepResults = new ArrayList<>();
        List<String> executedSteps = new ArrayList<>();
        List<String> failedSteps = new ArrayList<>();
        
        log.info("[CHAIN] 병렬 체인 처리 시작 - chainId: {}, steps: {}", chainId, steps.size());
        
        // 병렬 처리는 추후 CompletableFuture로 구현 예정
        // 현재는 순차 처리로 동작
        return executeSequentialChain(steps, sharedInput);
    }
    
    /**
     * Object를 Map으로 변환 (체인 입력용)
     */
    private Map<String, Object> convertToMap(Object obj) {
        if (obj instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) obj;
            return map;
        }
        
        // ObjectMapper를 사용한 변환 (추후 구현)
        throw new UnsupportedOperationException("DTO to Map 변환이 필요합니다");
    }
}