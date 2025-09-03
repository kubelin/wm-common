package com.samsung.common.service;

import com.samsung.common.response.CommonResponse;

import java.util.Map;

/**
 * 타입 안전한 모듈 서비스 인터페이스
 * Input/Output DTO를 지원하는 팩토리 패턴 서비스
 * Generic 타입 정보를 추가로 제공하는 확장 인터페이스
 * 
 * @param <I> Input DTO 타입
 * @param <O> Output DTO 타입
 */
public interface TypedModuleService<I, O> extends ModuleService {
    
    /**
     * 타입 안전한 모듈별 비즈니스 로직 실행
     * 
     * @param inputDto 입력 DTO (C 함수의 파라미터에 해당)
     * @return 출력 DTO (직접 반환, CommonResponse 래핑 없음)
     */
    O processTyped(I inputDto);
}