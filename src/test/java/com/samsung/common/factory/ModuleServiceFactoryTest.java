package com.samsung.common.factory;

import com.samsung.common.response.CommonResponse;
import com.samsung.common.service.ModuleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ModuleServiceFactory 테스트
 */
@SpringBootTest
public class ModuleServiceFactoryTest {
    
    @Autowired
    private ModuleServiceFactory factory;
    
    @Test
    public void Factory_빈_주입_테스트() {
        assertNotNull(factory);
    }
    
    @Test
    public void VM0001_서비스_조회_테스트() {
        // When
        ModuleService service = factory.getService("vm0001");
        
        // Then
        assertNotNull(service);
        assertEquals("vm0001", service.getServiceId());
        assertEquals("고객정보 조회 서비스", service.getDescription());
    }
    
    @Test
    public void VM0002_서비스_조회_테스트() {
        // When
        ModuleService service = factory.getService("vm0002");
        
        // Then
        assertNotNull(service);
        assertEquals("vm0002", service.getServiceId());
        assertEquals("계좌잔고 조회 서비스", service.getDescription());
    }
    
    @Test
    public void 존재하지않는_서비스_조회_테스트() {
        // When
        ModuleService service = factory.getService("vm9999");
        
        // Then
        assertNotNull(service);
        assertEquals("default", service.getServiceId());
        assertEquals("기본 서비스", service.getDescription());
    }
    
    @Test
    public void 기본서비스_동작_테스트() {
        // Given
        Map<String, Object> input = Map.of("test", "data");
        
        // When
        ModuleService service = factory.getService("vm9999");
        CommonResponse<?> response = service.process(input);
        
        // Then
        assertTrue(response.isSuccess());
        assertEquals("0000", response.getCode());
        assertEquals("기본 서비스 처리 완료", response.getMessage());
    }
    
    @Test
    public void 등록된_모든_서비스_확인_테스트() {
        // Given
        String[] expectedServiceIds = {"vm0001", "vm0002"};
        
        // When & Then
        for (String serviceId : expectedServiceIds) {
            ModuleService service = factory.getService(serviceId);
            assertNotNull(service, serviceId + " 서비스가 등록되어 있어야 함");
            assertEquals(serviceId, service.getServiceId());
        }
    }
}