package com.samsung.common.service;

import com.samsung.common.response.CommonResponse;

/**
 * 타입 안전한 모듈 서비스 인터페이스
 * Input/Output DTO를 지원하는 팩토리 패턴 서비스
 * 
 * @param <I> Input DTO 타입
 * @param <O> Output DTO 타입
 */
public interface TypedModuleService<I, O> extends ModuleService {
    
    /**
     * 타입 안전한 모듈별 비즈니스 로직 실행
     * 
     * @param inputDto 입력 DTO (C 함수의 파라미터에 해당)
     * @return 처리 결과 (출력 DTO)
     */
    CommonResponse<O> processTyped(I inputDto);
    
    /**
     * Input DTO 클래스 반환 (런타임에 타입 정보를 위해 사용)
     */
    Class<I> getInputDtoClass();
    
    /**
     * Output DTO 클래스 반환 (런타임에 타입 정보를 위해 사용)
     */
    Class<O> getOutputDtoClass();
}