package com.samsung.common.service;

import com.samsung.common.constants.ErrorCodes;
import com.samsung.common.event.ModuleEventPublisher;
import com.samsung.common.response.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

/**
 * 타입 안전한 추상 모듈 서비스 클래스
 * Input/Output DTO를 사용하는 Factory 패턴 서비스
 * BIZ 레이어에서 직접 호출 가능
 * 
 * 이벤트 주도 아키텍처 적용:
 * - C 함수의 깊은 호출(20~30 depth)을 Event Chain으로 단순화
 * - 각 처리 단계별 이벤트 발행으로 관찰 가능성 확보
 */
@Slf4j
public abstract class AbstractTypedModuleService<I, O> implements TypedModuleService<I, O> {
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Autowired(required = false)
    private ModuleEventPublisher eventPublisher;
    
    /**
     * Input DTO 클래스 반환 (구현체에서 정의)
     */
    protected abstract Class<I> getInputDtoClass();
    
    /**
     * Output DTO 클래스 반환 (구현체에서 정의)  
     */
    protected abstract Class<O> getOutputDtoClass();
    
    @Override
    @Transactional
    public CommonResponse<?> process(Map<String, Object> input) {
        String serviceId = getServiceId();
        long startTime = System.currentTimeMillis();
        String chainId = null;
        
        log.info("[{}] DTO 모듈 처리 시작 (HTTP용) - input: {}", serviceId, input);
        
        try {
            // Event: 모듈 처리 시작
            if (eventPublisher != null) {
                eventPublisher.publishModuleStarted(serviceId, input);
                chainId = "CHAIN-" + System.currentTimeMillis();
            }
            
            // 1. Map을 Input DTO로 변환
            I inputDto = convertToDto(input, getInputDtoClass());
            
            // Event: 입력 검증 완료
            if (eventPublisher != null) {
                eventPublisher.publishInputValidated(serviceId, inputDto, chainId);
            }
            
            // 2. 타입 안전한 비즈니스 로직 실행 (각 모듈에서 검증 처리)
            long businessStartTime = System.currentTimeMillis();
            O result = processTyped(inputDto);
            long businessExecutionTime = System.currentTimeMillis() - businessStartTime;
            
            // Event: 비즈니스 로직 처리 완료
            if (eventPublisher != null) {
                eventPublisher.publishBusinessProcessed(serviceId, result, chainId, businessExecutionTime);
            }
            
            long totalExecutionTime = System.currentTimeMillis() - startTime;
            
            // Event: 모듈 처리 완전 완료
            if (eventPublisher != null) {
                eventPublisher.publishModuleCompleted(serviceId, result, chainId, totalExecutionTime);
            }
            
            log.info("[{}] DTO 모듈 처리 완료 (HTTP용) - result: {} ({}ms)", serviceId, result, totalExecutionTime);
            return CommonResponse.success(result, serviceId + " 처리 완료");
            
        } catch (IllegalArgumentException e) {
            // Event: 에러 발생
            if (eventPublisher != null) {
                eventPublisher.publishModuleError(serviceId, e, chainId);
            }
            
            log.error("[{}] 입력 검증 오류: {}", serviceId, e.getMessage());
            return CommonResponse.error(ErrorCodes.INVALID_PARAMETER, e.getMessage());
            
        } catch (Exception e) {
            // Event: 에러 발생
            if (eventPublisher != null) {
                eventPublisher.publishModuleError(serviceId, e, chainId);
            }
            
            log.error("[{}] 처리 중 오류 발생", serviceId, e);
            return CommonResponse.error(ErrorCodes.UNKNOWN_ERROR, 
                                      serviceId + " 처리 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional
    @SuppressWarnings("unchecked")
    public <T> T process(Map<String, Object> input, Class<T> outputClass) {
        String serviceId = getServiceId();
        log.info("[{}] 타입 안전 DTO 모듈 처리 시작 (BIZ용) - input: {}, outputClass: {}", 
                serviceId, input, outputClass.getSimpleName());
        
        try {
            // 1. Map을 Input DTO로 변환
            I inputDto = convertToDto(input, getInputDtoClass());
            
            // 2. 타입 안전한 비즈니스 로직 실행 (각 모듈에서 검증 처리)
            O result = processTyped(inputDto);
            
            // 4. Output DTO가 요청된 타입과 일치하는지 확인하고 변환
            if (outputClass.isInstance(result)) {
                T typedResult = (T) result;
                log.info("[{}] 타입 안전 DTO 모듈 처리 완료 (BIZ용) - result: {}", serviceId, typedResult);
                return typedResult;
            } else {
                throw new IllegalArgumentException("출력 타입이 일치하지 않습니다. 요청: " + outputClass.getSimpleName() + 
                                                 ", 실제: " + result.getClass().getSimpleName());
            }
            
        } catch (IllegalArgumentException e) {
            log.error("[{}] 입력 검증 오류: {}", serviceId, e.getMessage());
            throw e;
            
        } catch (Exception e) {
            log.error("[{}] 처리 중 오류 발생", serviceId, e);
            throw new RuntimeException(serviceId + " 처리 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }
    
    
    /**
     * 타입 안전한 비즈니스 로직 실행 (각 구현체에서 구현)
     * 
     * @param inputDto 입력 DTO
     * @return 출력 DTO (직접 반환)
     */
    @Override
    public abstract O processTyped(I inputDto);
    
    // === DTO 변환 유틸리티 메소드들 ===
    
    /**
     * Map을 DTO로 변환
     */
    protected <T> T convertToDto(Map<String, Object> input, Class<T> dtoClass) {
        try {
            return objectMapper.convertValue(input, dtoClass);
        } catch (Exception e) {
            log.error("Map -> DTO 변환 중 오류 발생: {}", e.getMessage());
            throw new IllegalArgumentException("입력 데이터 변환에 실패했습니다: " + e.getMessage());
        }
    }
    
    /**
     * DTO를 Map으로 변환
     */
    @SuppressWarnings("unchecked")
    protected Map<String, Object> convertToMap(Object dto) {
        try {
            return objectMapper.convertValue(dto, Map.class);
        } catch (Exception e) {
            log.error("DTO -> Map 변환 중 오류 발생: {}", e.getMessage());
            throw new IllegalArgumentException("출력 데이터 변환에 실패했습니다: " + e.getMessage());
        }
    }
}