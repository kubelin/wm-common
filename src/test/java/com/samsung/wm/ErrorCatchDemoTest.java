package com.samsung.wm;

import com.samsung.common.factory.ModuleServiceFactory;
import com.samsung.common.response.CommonResponse;
import com.samsung.common.service.ModuleService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

/**
 * 모듈 에러 캐치 데모
 * 실제로 어떻게 에러가 캐치되는지 보여주는 테스트
 */
@Slf4j
@SpringBootTest
public class ErrorCatchDemoTest {

    @Autowired
    private ModuleServiceFactory factory;

    @Test
    public void 정상_케이스_테스트() {
        log.info("=== 정상 케이스 테스트 ===");
        
        // 정상적인 입력
        Map<String, Object> input = Map.of("customerId", "CUST000001");
        
        ModuleService service = factory.getService("vm0001");
        CommonResponse<?> response = service.process(input);
        
        log.info("결과: {}", response);
        // 정상 처리 시: success=true, code="0000"
    }
    
    @Test 
    public void 입력_검증_에러_케이스() {
        log.info("=== 입력 검증 에러 케이스 ===");
        
        // 잘못된 입력 (길이 오류)
        Map<String, Object> input = Map.of("customerId", "SHORT");
        
        ModuleService service = factory.getService("vm0001");
        CommonResponse<?> response = service.process(input);
        
        log.info("결과: {}", response);
        // 에러 처리 결과: success=false, code="0002" (INVALID_PARAMETER)
        
        // 실행 순서:
        // 1. vm0001.processTyped() 실행
        // 2. validateInput()에서 IllegalArgumentException 발생  
        // 3. AbstractTypedModuleService.process()의 catch 블록에서 캐치
        // 4. handleError() 메서드 호출
        // 5. ModuleErrorHandler.handleException()에서 에러 분류
        // 6. CommonResponse.error()로 응답 생성
    }
    
    @Test
    public void 비즈니스_로직_에러_케이스() {
        log.info("=== 비즈니스 로직 에러 케이스 ===");
        
        // 존재하지 않는 고객
        Map<String, Object> input = Map.of("customerId", "NOTFOUND01");
        
        ModuleService service = factory.getService("vm0001");
        CommonResponse<?> response = service.process(input);
        
        log.info("결과: {}", response);
        // 에러 처리 결과: success=false, code="6001" (CUSTOMER_NOT_FOUND)
        
        // 실행 순서:
        // 1. vm0001.processTyped() 실행
        // 2. vm0001Dao.selectCustomer()에서 null 반환
        // 3. ModuleException.businessLogicError() 발생
        // 4. AbstractTypedModuleService.process()의 catch 블록에서 캐치  
        // 5. handleError()에서 ModuleException 그대로 처리
        // 6. 이미 분류된 에러코드로 응답 생성
    }
    
    @Test
    public void 데이터베이스_에러_케이스() {
        log.info("=== 데이터베이스 에러 케이스 ===");
        
        // 데이터베이스 에러를 시뮬레이션하기 어려우므로 설명만
        log.info("DB 연결 실패 시:");
        log.info("1. vm0001Dao.selectCustomer()에서 SQLException 발생");  
        log.info("2. AbstractTypedModuleService.process()의 catch (Exception e) 블록에서 캐치");
        log.info("3. handleError() → ModuleErrorHandler.handleException() 호출");
        log.info("4. isDatabaseError() 체크로 DB 에러로 분류");
        log.info("5. ModuleException.databaseError() 생성 (CRITICAL 레벨)");
        log.info("6. CommonResponse.error(7001, 'DB 에러 메시지') 응답");
    }
    
    @Test
    public void 존재하지_않는_모듈_케이스() {
        log.info("=== 존재하지 않는 모듈 케이스 ===");
        
        Map<String, Object> input = Map.of("test", "data");
        
        // 존재하지 않는 모듈 ID
        ModuleService service = factory.getService("vm9999");
        CommonResponse<?> response = service.process(input);
        
        log.info("결과: {}", response);
        // DefaultModuleService가 실행되어 기본 응답 반환
    }
}