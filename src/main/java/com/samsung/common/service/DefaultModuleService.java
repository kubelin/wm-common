package com.samsung.common.service;

import com.samsung.common.response.CommonResponse;

import java.util.Map;

/**
 * 기본 모듈 서비스
 * 서비스를 찾을 수 없을 때 사용되는 기본 구현체
 */
public class DefaultModuleService implements ModuleService {
    
    @Override
    public String getServiceId() {
        return "default";
    }
    
    @Override
    public CommonResponse<?> process(Map<String, Object> input) {
        return CommonResponse.error(
            "SERVICE_NOT_FOUND", 
            "해당 서비스를 찾을 수 없습니다"
        );
    }
    
    @Override
    public <T> T process(Map<String, Object> input, Class<T> outputClass) {
        throw new RuntimeException("해당 서비스를 찾을 수 없습니다");
    }
    
    @Override
    public String getDescription() {
        return "기본 서비스 (서비스를 찾을 수 없을 때 사용)";
    }
}