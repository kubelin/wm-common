package com.samsung.common.service;

import com.samsung.common.response.CommonResponse;

import java.util.Map;

/**
 * 기본 모듈 서비스 인터페이스
 * Generic 없는 심플한 사용을 위한 베이스 인터페이스
 * Controller와 일반적인 사용에 적합
 */
public interface ModuleService {
    
    /**
     * 서비스 ID 반환 (예: "vm0001", "vm0002")
     */
    String getServiceId();
    
    /**
     * 모듈별 비즈니스 로직 실행 (HTTP 응답용 - CommonResponse 래핑)
     * 
     * @param input 입력 데이터 (C 함수의 파라미터에 해당)
     * @return 처리 결과 (CommonResponse 래핑)
     */
    CommonResponse<?> process(Map<String, Object> input);
    
    /**
     * 타입 안전한 모듈별 비즈니스 로직 실행 (BIZ 레이어용 - 직접 타입 반환)
     * 
     * @param input 입력 데이터
     * @param outputClass 출력 클래스 타입
     * @param <T> 출력 타입
     * @return 처리 결과 (직접 타입 반환)
     */
    <T> T process(Map<String, Object> input, Class<T> outputClass);
    
    /**
     * 서비스 설명 (선택적)
     */
    default String getDescription() {
        return getServiceId() + " 모듈 서비스";
    }
}