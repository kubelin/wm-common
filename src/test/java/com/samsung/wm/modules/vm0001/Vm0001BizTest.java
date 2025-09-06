package com.samsung.wm.modules.vm0001;

import com.samsung.common.factory.ModuleServiceFactory;
import com.samsung.common.response.CommonResponse;
import com.samsung.common.service.ModuleService;
import com.samsung.wm.modules.vm0001.dto.Vm0001InputDto;
import com.samsung.wm.modules.vm0001.dto.Vm0001OutputDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * VM0001 모듈 서비스 테스트
 */
@SpringBootTest
public class Vm0001BizTest {
    
    @Autowired
    private ModuleServiceFactory factory;
    
    @Autowired
    private Vm0001Biz vm0001Biz;
    
    @Test
    public void 정상_고객조회_테스트() {
        // Given
        Map<String, Object> input = Map.of("customerId", "CUST000001");
        
        // When
        ModuleService service = factory.getService("vm0001");
        CommonResponse<?> response = service.process(input);
        
        // Then
        assertTrue(response.isSuccess());
        assertEquals("0000", response.getCode());
        assertNotNull(response.getData());
    }
    
    @Test
    public void 입력검증_에러_테스트() {
        // Given - 잘못된 고객ID (길이 오류)
        Map<String, Object> input = Map.of("customerId", "SHORT");
        
        // When
        ModuleService service = factory.getService("vm0001");
        CommonResponse<?> response = service.process(input);
        
        // Then
        assertFalse(response.isSuccess());
        assertEquals("0002", response.getCode());
        assertNull(response.getData());
    }
    
    @Test
    public void 고객없음_에러_테스트() {
        // Given - 존재하지 않는 고객
        Map<String, Object> input = Map.of("customerId", "NOTFOUND01");
        
        // When
        ModuleService service = factory.getService("vm0001");
        CommonResponse<?> response = service.process(input);
        
        // Then
        assertFalse(response.isSuccess());
        assertEquals("6001", response.getCode());
        assertNull(response.getData());
    }
    
    @Test
    public void 타입안전_BIZ용_메서드_테스트() {
        // Given
        Vm0001InputDto inputDto = new Vm0001InputDto();
        inputDto.setCustomerId("CUST000001");
        
        // When
        Vm0001OutputDto result = vm0001Biz.processTyped(inputDto);
        
        // Then
        assertNotNull(result);
        assertEquals("200", result.getResultCode());
        assertEquals("고객정보 조회 성공", result.getMessage());
        assertNotNull(result.getCustomerInfo());
        assertNotNull(result.getAccessTime());
    }
    
    @Test
    public void BIZ용_타입안전_프로세스_테스트() {
        // Given
        Map<String, Object> input = Map.of("customerId", "CUST000001");
        
        // When
        Vm0001OutputDto result = vm0001Biz.process(input, Vm0001OutputDto.class);
        
        // Then
        assertNotNull(result);
        assertEquals("200", result.getResultCode());
        assertNotNull(result.getCustomerInfo());
    }
}