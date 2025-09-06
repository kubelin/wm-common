package com.samsung.common.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * 모듈 이벤트 발행자
 * C 함수의 깊은 호출을 Event Chain으로 변환
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ModuleEventPublisher {
    
    private final ApplicationEventPublisher eventPublisher;
    
    /**
     * 모듈 처리 시작 이벤트 발행
     */
    public void publishModuleStarted(String serviceId, Object inputData) {
        ModuleProcessEvent event = ModuleProcessEvent.builder()
                .eventType(ModuleProcessEvent.EventType.MODULE_STARTED)
                .serviceId(serviceId)
                .resultData(inputData)
                .timestamp(LocalDateTime.now())
                .chainId(generateChainId())
                .build();
        
        log.info("[EVENT] 모듈 처리 시작: {}", serviceId);
        eventPublisher.publishEvent(event);
    }
    
    /**
     * 입력 검증 완료 이벤트 발행
     */
    public void publishInputValidated(String serviceId, Object validatedInput, String chainId) {
        ModuleProcessEvent event = ModuleProcessEvent.builder()
                .eventType(ModuleProcessEvent.EventType.INPUT_VALIDATED)
                .serviceId(serviceId)
                .resultData(validatedInput)
                .timestamp(LocalDateTime.now())
                .chainId(chainId)
                .metadata(Map.of("step", "validation", "status", "success"))
                .build();
        
        log.info("[EVENT] 입력 검증 완료: {}", serviceId);
        eventPublisher.publishEvent(event);
    }
    
    /**
     * 고객 검증 완료 이벤트 발행
     */
    public void publishCustomerValidated(String serviceId, Object customerData, String chainId) {
        ModuleProcessEvent event = ModuleProcessEvent.builder()
                .eventType(ModuleProcessEvent.EventType.CUSTOMER_VALIDATED)
                .serviceId(serviceId)
                .resultData(customerData)
                .timestamp(LocalDateTime.now())
                .chainId(chainId)
                .metadata(Map.of("step", "customer_validation", "status", "success"))
                .build();
        
        log.info("[EVENT] 고객 검증 완료: {}", serviceId);
        eventPublisher.publishEvent(event);
    }
    
    /**
     * 비즈니스 로직 처리 완료 이벤트 발행
     */
    public void publishBusinessProcessed(String serviceId, Object processResult, String chainId, Long executionTime) {
        ModuleProcessEvent event = ModuleProcessEvent.builder()
                .eventType(ModuleProcessEvent.EventType.BUSINESS_PROCESSED)
                .serviceId(serviceId)
                .resultData(processResult)
                .timestamp(LocalDateTime.now())
                .chainId(chainId)
                .executionTime(executionTime)
                .metadata(Map.of("step", "business_logic", "status", "success"))
                .build();
        
        log.info("[EVENT] 비즈니스 로직 처리 완료: {} ({}ms)", serviceId, executionTime);
        eventPublisher.publishEvent(event);
    }
    
    /**
     * 데이터베이스 처리 완료 이벤트 발행
     */
    public void publishDatabaseProcessed(String serviceId, Object dbResult, String chainId) {
        ModuleProcessEvent event = ModuleProcessEvent.builder()
                .eventType(ModuleProcessEvent.EventType.DATABASE_PROCESSED)
                .serviceId(serviceId)
                .resultData(dbResult)
                .timestamp(LocalDateTime.now())
                .chainId(chainId)
                .metadata(Map.of("step", "database", "status", "success"))
                .build();
        
        log.info("[EVENT] 데이터베이스 처리 완료: {}", serviceId);
        eventPublisher.publishEvent(event);
    }
    
    /**
     * 모듈 처리 완전 완료 이벤트 발행
     */
    public void publishModuleCompleted(String serviceId, Object finalResult, String chainId, Long totalExecutionTime) {
        ModuleProcessEvent event = ModuleProcessEvent.builder()
                .eventType(ModuleProcessEvent.EventType.MODULE_COMPLETED)
                .serviceId(serviceId)
                .resultData(finalResult)
                .timestamp(LocalDateTime.now())
                .chainId(chainId)
                .executionTime(totalExecutionTime)
                .metadata(Map.of("step", "completed", "status", "success", "total_time", totalExecutionTime))
                .build();
        
        log.info("[EVENT] 모듈 처리 완전 완료: {} (총 {}ms)", serviceId, totalExecutionTime);
        eventPublisher.publishEvent(event);
    }
    
    /**
     * 체인 처리 완료 이벤트 발행 (여러 모듈 연계)
     */
    public void publishChainCompleted(String chainId, Object chainResult, Long totalExecutionTime) {
        ModuleProcessEvent event = ModuleProcessEvent.builder()
                .eventType(ModuleProcessEvent.EventType.CHAIN_COMPLETED)
                .serviceId("CHAIN")
                .resultData(chainResult)
                .timestamp(LocalDateTime.now())
                .chainId(chainId)
                .executionTime(totalExecutionTime)
                .metadata(Map.of("step", "chain_completed", "status", "success", "total_time", totalExecutionTime))
                .build();
        
        log.info("[EVENT] 체인 처리 완료: {} (총 {}ms)", chainId, totalExecutionTime);
        eventPublisher.publishEvent(event);
    }
    
    /**
     * 에러 이벤트 발행
     */
    public void publishModuleError(String serviceId, Exception error, String chainId) {
        ModuleProcessEvent event = ModuleProcessEvent.builder()
                .eventType(ModuleProcessEvent.EventType.MODULE_ERROR)
                .serviceId(serviceId)
                .error(error)
                .timestamp(LocalDateTime.now())
                .chainId(chainId)
                .metadata(Map.of("step", "error", "status", "failed", "error_message", error.getMessage()))
                .build();
        
        log.error("[EVENT] 모듈 처리 에러: {} - {}", serviceId, error.getMessage());
        eventPublisher.publishEvent(event);
    }
    
    /**
     * 체인 ID 생성
     */
    private String generateChainId() {
        return "CHAIN-" + UUID.randomUUID().toString().substring(0, 8);
    }
}