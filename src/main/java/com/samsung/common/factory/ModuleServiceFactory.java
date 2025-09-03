package com.samsung.common.factory;

import com.samsung.common.service.ModuleService;
import com.samsung.common.service.TypedModuleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

/**
 * 모듈 서비스 팩토리
 * ServiceId를 기반으로 적절한 ModuleService 구현체를 반환
 * Raw 타입 사용으로 심플한 인터페이스 제공
 */
@Slf4j
@Component
public class ModuleServiceFactory {
    
    private final Map<String, ModuleService> services;
    private final ModuleService defaultService;
    
    public ModuleServiceFactory(List<TypedModuleService<?,?>> serviceList) {
        log.info("ModuleServiceFactory 초기화 - 등록된 서비스 수: {}", serviceList.size());
        
        // Spring이 자동으로 모든 TypedModuleService 구현체들을 주입
        // ModuleService 타입으로 캐스팅해서 저장 (TypedModuleService extends ModuleService)
        this.services = serviceList.stream()
                .collect(toMap(ModuleService::getServiceId, 
                              service -> (ModuleService) service));
        
        // 기본 서비스 (서비스를 찾을 수 없을 때 사용)
        this.defaultService = new com.samsung.common.service.DefaultModuleService();
        
        // 등록된 서비스들 로그 출력
        services.keySet().forEach(serviceId -> 
            log.info("등록된 모듈 서비스: {} - {}", serviceId, services.get(serviceId).getDescription())
        );
    }
    
    /**
     * ServiceId로 ModuleService 조회 (Raw 타입으로 심플하게 사용)
     */
    public ModuleService getService(String serviceId) {
        ModuleService service = services.get(serviceId);
        if (service == null) {
            log.warn("서비스를 찾을 수 없습니다: {} - 기본 서비스 사용", serviceId);
            return defaultService;
        }
        
        log.debug("서비스 조회: {} -> {}", serviceId, service.getClass().getSimpleName());
        return service;
    }
    
    /**
     * 등록된 모든 서비스 ID 목록 조회
     */
    public Map<String, String> getAllServices() {
        Map<String, String> result = new HashMap<>();
        services.forEach((serviceId, service) -> 
            result.put(serviceId, service.getDescription())
        );
        return result;
    }
    
    /**
     * 등록된 서비스 개수
     */
    public int getServiceCount() {
        return services.size();
    }
    
    /**
     * 서비스 존재 여부 확인
     */
    public boolean hasService(String serviceId) {
        return services.containsKey(serviceId);
    }
}