package com.samsung.wm.common.service;

import com.samsung.wm.common.response.CommonResponse;

import java.util.Map;

/**
 * 모듈 서비스 인터페이스
 * 모든 C 파일 변환 서비스가 구현해야 하는 공통 인터페이스
 */
public interface ModuleService {
    
    /**
     * 서비스 ID 반환 (예: "vm0001", "vm0002")
     */
    String getServiceId();
    
    /**
     * 모듈별 비즈니스 로직 실행
     * 
     * @param input 입력 데이터 (C 함수의 파라미터에 해당)
     * @return 처리 결과
     */
    CommonResponse<?> process(Map<String, Object> input);
    
    /**
     * 서비스 설명 (선택적)
     */
    default String getDescription() {
        return getServiceId() + " 모듈 서비스";
    }
}