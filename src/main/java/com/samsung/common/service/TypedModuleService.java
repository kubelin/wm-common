package com.samsung.common.service;

/**
 * 타입 안전한 모듈 서비스 인터페이스
 * Input/Output DTO를 지원하는 팩토리 패턴 서비스
 * BIZ 레이어에서 직접 호출하여 Output DTO를 바로 받을 수 있음
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
    
    /**
     * Input DTO 클래스 반환 (런타임에 타입 정보를 위해 사용)
     */
    Class<I> getInputDtoClass();
    
    /**
     * Output DTO 클래스 반환 (런타임에 타입 정보를 위해 사용)
     */
    Class<O> getOutputDtoClass();
}