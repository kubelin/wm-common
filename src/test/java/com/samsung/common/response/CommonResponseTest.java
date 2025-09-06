package com.samsung.common.response;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * CommonResponse 테스트
 */
public class CommonResponseTest {
    
    @Test
    public void 성공_응답_생성_테스트() {
        // Given
        String testData = "test result";
        String message = "처리 성공";
        
        // When
        CommonResponse<String> response = CommonResponse.success(testData, message);
        
        // Then
        assertTrue(response.isSuccess());
        assertEquals("0000", response.getCode());
        assertEquals(message, response.getMessage());
        assertEquals(testData, response.getData());
        assertNotNull(response.getTimestamp());
    }
    
    @Test
    public void 성공_응답_데이터없음_테스트() {
        // Given
        String message = "처리 완료";
        
        // When
        CommonResponse<Object> response = CommonResponse.success(message);
        
        // Then
        assertTrue(response.isSuccess());
        assertEquals("0000", response.getCode());
        assertEquals(message, response.getMessage());
        assertNull(response.getData());
    }
    
    @Test
    public void 에러_응답_생성_테스트() {
        // Given
        String errorCode = "0002";
        String errorMessage = "입력값이 잘못되었습니다";
        
        // When
        CommonResponse<Object> response = CommonResponse.error(errorCode, errorMessage);
        
        // Then
        assertFalse(response.isSuccess());
        assertEquals(errorCode, response.getCode());
        assertEquals(errorMessage, response.getMessage());
        assertNull(response.getData());
        assertNotNull(response.getTimestamp());
    }
    
    @Test
    public void 복합객체_데이터_테스트() {
        // Given
        Map<String, Object> complexData = Map.of(
            "customerId", "CUST000001",
            "accountCount", 3,
            "totalBalance", 1000000,
            "lastAccess", LocalDateTime.now()
        );
        
        // When
        CommonResponse<Map<String, Object>> response = CommonResponse.success(complexData, "조회 성공");
        
        // Then
        assertTrue(response.isSuccess());
        assertEquals("0000", response.getCode());
        assertNotNull(response.getData());
        assertEquals("CUST000001", response.getData().get("customerId"));
        assertEquals(3, response.getData().get("accountCount"));
    }
    
    @Test
    public void JSON_직렬화_필드_확인() {
        // Given
        CommonResponse<String> response = CommonResponse.success("test", "success");
        
        // When & Then - 필드들이 올바르게 설정되었는지 확인
        assertNotNull(response.isSuccess());
        assertNotNull(response.getCode());
        assertNotNull(response.getMessage());
        assertNotNull(response.getData());
        assertNotNull(response.getTimestamp());
    }
    
    @Test
    public void 타임스탬프_생성_확인() {
        // Given
        LocalDateTime beforeCreate = LocalDateTime.now().minusSeconds(1);
        
        // When
        CommonResponse<String> response = CommonResponse.success("test", "success");
        LocalDateTime afterCreate = LocalDateTime.now().plusSeconds(1);
        
        // Then
        assertTrue(response.getTimestamp().isAfter(beforeCreate));
        assertTrue(response.getTimestamp().isBefore(afterCreate));
    }
}