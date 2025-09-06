package com.samsung.wm.modules.vm0002;

import com.samsung.common.factory.ModuleServiceFactory;
import com.samsung.common.response.CommonResponse;
import com.samsung.common.service.ModuleService;
import com.samsung.wm.modules.vm0002.dto.Vm0002InputDto;
import com.samsung.wm.modules.vm0002.dto.Vm0002OutputDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * VM0002 모듈 서비스 테스트
 */
@SpringBootTest
public class Vm0002BizTest {
    
    @Autowired
    private ModuleServiceFactory factory;
    
    @Autowired
    private Vm0002Biz vm0002Biz;
    
    @Test
    public void 정상_계좌잔고조회_테스트() {
        // Given
        Map<String, Object> input = Map.of(
            "customerId", "CUST000001",
            "accountType", "SAVINGS"
        );
        
        // When
        ModuleService service = factory.getService("vm0002");
        CommonResponse<?> response = service.process(input);
        
        // Then
        assertTrue(response.isSuccess());
        assertEquals("0000", response.getCode());
        assertNotNull(response.getData());
    }
    
    @Test
    public void 전체계좌조회_테스트() {
        // Given
        Map<String, Object> input = Map.of(
            "customerId", "CUST000001",
            "accountType", "ALL"
        );
        
        // When
        ModuleService service = factory.getService("vm0002");
        CommonResponse<?> response = service.process(input);
        
        // Then
        assertTrue(response.isSuccess());
        assertEquals("0000", response.getCode());
    }
    
    @Test
    public void 입력검증_에러_테스트() {
        // Given - 잘못된 계좌유형
        Map<String, Object> input = Map.of(
            "customerId", "CUST000001",
            "accountType", "INVALID"
        );
        
        // When
        ModuleService service = factory.getService("vm0002");
        CommonResponse<?> response = service.process(input);
        
        // Then
        assertFalse(response.isSuccess());
        assertEquals("0002", response.getCode());
    }
    
    @Test
    public void 타입안전_BIZ용_메서드_테스트() {
        // Given
        Vm0002InputDto inputDto = new Vm0002InputDto();
        inputDto.setCustomerId("CUST000001");
        inputDto.setAccountType("SAVINGS");
        
        // When
        Vm0002OutputDto result = vm0002Biz.processTyped(inputDto);
        
        // Then
        assertNotNull(result);
        assertEquals("200", result.getResultCode());
        assertEquals("계좌잔고 조회 성공", result.getMessage());
        assertNotNull(result.getAccounts());
        assertTrue(result.getAccountCount() >= 0);
        assertNotNull(result.getTotalBalance());
        assertTrue(result.getTotalBalance().compareTo(BigDecimal.ZERO) >= 0);
    }
    
    @Test
    public void BIZ용_타입안전_프로세스_테스트() {
        // Given
        Map<String, Object> input = Map.of(
            "customerId", "CUST000001",
            "accountType", "CHECKING"
        );
        
        // When
        Vm0002OutputDto result = vm0002Biz.process(input, Vm0002OutputDto.class);
        
        // Then
        assertNotNull(result);
        assertEquals("200", result.getResultCode());
        assertNotNull(result.getAccounts());
    }
}