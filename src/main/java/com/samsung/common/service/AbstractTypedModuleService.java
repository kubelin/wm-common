package com.samsung.common.service;

import com.samsung.common.constants.ErrorCodes;
import com.samsung.common.response.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.Set;

/**
 * 타입 안전한 추상 모듈 서비스 클래스
 * Input/Output DTO를 사용하는 Factory 패턴 서비스
 * BIZ 레이어에서 직접 호출 가능
 */
@Slf4j
public abstract class AbstractTypedModuleService<I, O> extends AbstractModuleService 
        implements TypedModuleService<I, O> {
    
    @Autowired(required = false)
    private Validator validator;
    
    @Override
    @Transactional
    public CommonResponse<?> process(Map<String, Object> input) {
        String serviceId = getServiceId();
        log.info("[{}] DTO 모듈 처리 시작 - input: {}", serviceId, input);
        
        try {
            // 1. Map을 Input DTO로 변환
            I inputDto = convertToDto(input, getInputDtoClass());
            
            // 2. DTO 검증 (Jakarta Validation)
            validateDto(inputDto);
            
            // 3. 타입 안전한 비즈니스 로직 실행
            O result = processTyped(inputDto);
            
            log.info("[{}] DTO 모듈 처리 완료 - result: {}", serviceId, result);
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
     * Jakarta Validation을 사용한 DTO 검증
     */
    protected void validateDto(I inputDto) {
        if (validator == null) {
            return; // Validator가 없으면 검증 스킵
        }
        
        Set<ConstraintViolation<I>> violations = validator.validate(inputDto);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder("입력 데이터 검증 실패: ");
            for (ConstraintViolation<I> violation : violations) {
                sb.append(violation.getMessage()).append("; ");
            }
            throw new IllegalArgumentException(sb.toString());
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
    
    // === 기존 Map 기반 메소드들을 오버라이드하여 사용하지 않도록 함 ===
    
    @Override
    protected void validateInput(Map<String, Object> input) {
        // DTO 검증을 사용하므로 이 메소드는 사용하지 않음
    }
    
    @Override
    protected Object executeBusinessLogic(Map<String, Object> input) {
        // processTyped()를 사용하므로 이 메소드는 사용하지 않음
        throw new UnsupportedOperationException("TypedModuleService는 processTyped() 메소드를 사용하세요");
    }
}