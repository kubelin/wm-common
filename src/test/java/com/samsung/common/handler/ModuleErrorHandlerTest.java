package com.samsung.common.handler;

import com.samsung.common.constants.ErrorCodes;
import com.samsung.common.exception.ModuleException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ModuleErrorHandler 테스트
 */
@ExtendWith(MockitoExtension.class)
public class ModuleErrorHandlerTest {
    
    private ModuleErrorHandler errorHandler;
    
    @BeforeEach
    void setUp() {
        errorHandler = new ModuleErrorHandler();
    }
    
    @Test
    public void IllegalArgumentException_처리_테스트() {
        // Given
        String serviceId = "test001";
        IllegalArgumentException originalException = new IllegalArgumentException("잘못된 입력입니다");
        Object inputData = "test input";
        
        // When
        ModuleException result = errorHandler.handleException(serviceId, originalException, inputData);
        
        // Then
        assertEquals(ErrorCodes.INVALID_PARAMETER, result.getErrorCode());
        assertEquals(serviceId, result.getServiceId());
        assertEquals("잘못된 입력입니다", result.getMessage());
        assertEquals(ModuleException.ErrorPhase.INPUT_VALIDATION, result.getErrorPhase());
        assertEquals(ModuleException.ErrorSeverity.WARN, result.getSeverity());
        assertTrue(result.isRecoverable());
    }
    
    @Test
    public void SQLException_처리_테스트() {
        // Given
        String serviceId = "test001";
        SQLException sqlException = new SQLException("Connection timeout");
        Object inputData = "test input";
        
        // When
        ModuleException result = errorHandler.handleException(serviceId, sqlException, inputData);
        
        // Then
        assertEquals(ErrorCodes.DATABASE_ERROR, result.getErrorCode());
        assertEquals(serviceId, result.getServiceId());
        assertTrue(result.getMessage().contains("데이터베이스"));
        assertEquals(ModuleException.ErrorPhase.DATABASE_ACCESS, result.getErrorPhase());
        assertEquals(ModuleException.ErrorSeverity.CRITICAL, result.getSeverity());
        assertTrue(result.isRecoverable());
    }
    
    @Test
    public void ModuleException_그대로_반환_테스트() {
        // Given
        String serviceId = "test001";
        ModuleException moduleException = ModuleException.businessLogicError(
            serviceId, ErrorCodes.CUSTOMER_NOT_FOUND, "고객을 찾을 수 없습니다", "input"
        );
        
        // When
        ModuleException result = errorHandler.handleException(serviceId, moduleException, "input");
        
        // Then
        assertEquals(moduleException, result);
        assertEquals(ErrorCodes.CUSTOMER_NOT_FOUND, result.getErrorCode());
        assertEquals(serviceId, result.getServiceId());
    }
    
    @Test
    public void 일반_Exception_처리_테스트() {
        // Given
        String serviceId = "test001";
        RuntimeException generalException = new RuntimeException("예상치 못한 오류");
        Object inputData = "test input";
        
        // When
        ModuleException result = errorHandler.handleException(serviceId, generalException, inputData);
        
        // Then
        assertEquals(ErrorCodes.UNKNOWN_ERROR, result.getErrorCode());
        assertEquals(serviceId, result.getServiceId());
        assertTrue(result.getMessage().contains("시스템 오류"));
        assertEquals(ModuleException.ErrorPhase.UNKNOWN, result.getErrorPhase());
        assertEquals(ModuleException.ErrorSeverity.ERROR, result.getSeverity());
        assertFalse(result.isRecoverable());
    }
    
    @Test
    public void getErrorCode_테스트() {
        // Given
        ModuleException moduleException = new ModuleException(
            ErrorCodes.CUSTOMER_NOT_FOUND, "test", "message",
            ModuleException.ErrorPhase.BUSINESS_LOGIC, null, true,
            ModuleException.ErrorSeverity.WARN
        );
        
        // When
        String errorCode = errorHandler.getErrorCode(moduleException);
        
        // Then
        assertEquals(ErrorCodes.CUSTOMER_NOT_FOUND, errorCode);
    }
    
    @Test
    public void logError_심각도별_테스트() {
        // Given
        String serviceId = "test001";
        
        ModuleException infoException = new ModuleException(
            "0000", serviceId, "info message",
            ModuleException.ErrorPhase.BUSINESS_LOGIC, null, true,
            ModuleException.ErrorSeverity.INFO
        );
        
        ModuleException criticalException = new ModuleException(
            "7001", serviceId, "critical message",
            ModuleException.ErrorPhase.DATABASE_ACCESS, null, false,
            ModuleException.ErrorSeverity.CRITICAL
        );
        
        // When & Then (로그 출력 확인)
        assertDoesNotThrow(() -> errorHandler.logError(serviceId, infoException));
        assertDoesNotThrow(() -> errorHandler.logError(serviceId, criticalException));
    }
}