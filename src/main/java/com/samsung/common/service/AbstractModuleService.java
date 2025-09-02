package com.samsung.common.service;

import com.samsung.common.constants.ErrorCodes;
import com.samsung.common.util.StringUtil;
import com.samsung.common.response.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

/**
 * 추상 모듈 서비스 클래스
 * 모든 C 파일 변환 서비스의 공통 로직을 처리
 * Input/Output DTO 패턴 지원
 */
@Slf4j
public abstract class AbstractModuleService implements ModuleService {
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Override
    @Transactional
    public CommonResponse<?> process(Map<String, Object> input) {
        String serviceId = getServiceId();
        log.info("[{}] 모듈 처리 시작 - input: {}", serviceId, input);
        
        try {
            // 1. 공통 입력 검증
            validateCommonInput(input);
            
            // 2. 개별 검증 (각 구현체에서 정의)
            validateInput(input);
            
            // 3. 비즈니스 로직 실행 (각 구현체에서 정의)
            Object result = executeBusinessLogic(input);
            
            log.info("[{}] 모듈 처리 완료 - result: {}", serviceId, result);
            return CommonResponse.success(result, serviceId + " 처리 완료");
            
        } catch (IllegalArgumentException e) {
            log.error("[{}] 입력 검증 오류: {}", serviceId, e.getMessage());
            return CommonResponse.error(ErrorCodes.INVALID_PARAMETER, e.getMessage());
            
        } catch (Exception e) {
            log.error("[{}] 처리 중 오류 발생", serviceId, e);
            return CommonResponse.error(ErrorCodes.UNKNOWN_ERROR, 
                                      serviceId + " 처리 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
    
    /**
     * 공통 입력 검증
     */
    protected void validateCommonInput(Map<String, Object> input) {
        if (input == null || input.isEmpty()) {
            throw new IllegalArgumentException("입력 데이터가 필요합니다");
        }
    }
    
    /**
     * 개별 입력 검증 (각 구현체에서 오버라이드)
     */
    protected abstract void validateInput(Map<String, Object> input);
    
    /**
     * 비즈니스 로직 실행 (각 구현체에서 구현)
     */
    protected abstract Object executeBusinessLogic(Map<String, Object> input);
    
    // === 공통 검증 유틸리티 메소드들 ===
    
    protected void requireField(Map<String, Object> input, String fieldName) {
        Object value = input.get(fieldName);
        if (value == null || (value instanceof String && StringUtil.isEmpty((String) value))) {
            throw new IllegalArgumentException(fieldName + "은(는) 필수 항목입니다");
        }
    }
    
    protected void requireNumeric(Map<String, Object> input, String fieldName) {
        requireField(input, fieldName);
        Object value = input.get(fieldName);
        if (!(value instanceof Number) && !(value instanceof String && isNumeric((String) value))) {
            throw new IllegalArgumentException(fieldName + "은(는) 숫자여야 합니다");
        }
    }
    
    protected void requireLength(Map<String, Object> input, String fieldName, int expectedLength) {
        requireField(input, fieldName);
        String value = String.valueOf(input.get(fieldName));
        if (value.length() != expectedLength) {
            throw new IllegalArgumentException(
                fieldName + "은(는) " + expectedLength + "자리여야 합니다 (현재: " + value.length() + "자리)");
        }
    }
    
    protected void requireMaxLength(Map<String, Object> input, String fieldName, int maxLength) {
        requireField(input, fieldName);
        String value = String.valueOf(input.get(fieldName));
        if (value.length() > maxLength) {
            throw new IllegalArgumentException(
                fieldName + "은(는) " + maxLength + "자 이하여야 합니다 (현재: " + value.length() + "자)");
        }
    }
    
    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
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
    protected Map<String, Object> convertToMap(Object dto) {
        try {
            return objectMapper.convertValue(dto, Map.class);
        } catch (Exception e) {
            log.error("DTO -> Map 변환 중 오류 발생: {}", e.getMessage());
            throw new IllegalArgumentException("출력 데이터 변환에 실패했습니다: " + e.getMessage());
        }
    }
}