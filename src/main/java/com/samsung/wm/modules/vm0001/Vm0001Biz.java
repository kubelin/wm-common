package com.samsung.wm.modules.vm0001;

import com.samsung.wm.common.dao.CommonDAO;
import com.samsung.wm.common.service.AbstractModuleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * VM0001 모듈 서비스 (고객정보 조회)
 * 
 * C 파일: vm0001.c, vm0001.h
 * 기능: 고객 정보 조회 및 접근 로그 기록
 */
@Slf4j
@Service
public class Vm0001Biz extends AbstractModuleService {
    
    public Vm0001Biz(CommonDAO dao) {
        super(dao);
    }
    
    @Override
    public String getServiceId() {
        return "vm0001";
    }
    
    @Override
    public String getDescription() {
        return "고객정보 조회 서비스";
    }
    
    @Override
    protected void validateInput(Map<String, Object> input) {
        // 고객 ID는 필수이며 10자리 숫자
        requireField(input, "customerId");
        requireLength(input, "customerId", 7); // CUST001 형태
    }
    
    @Override
    protected Object executeBusinessLogic(Map<String, Object> input) {
        String customerId = (String) input.get("customerId");
        log.info("[vm0001] 고객정보 조회 시작 - customerId: {}", customerId);
        
        try {
            // 1. 고객 정보 조회 (vm0001.c의 select_customer 함수 역할)
            Map<String, Object> customer = dao.selectOne(
                "vm0001.selectCustomer", 
                Map.of("customerId", customerId), 
                Map.class
            );
            
            if (customer == null) {
                log.warn("[vm0001] 고객을 찾을 수 없습니다 - customerId: {}", customerId);
                Map<String, Object> notFoundResult = new HashMap<>();
                notFoundResult.put("resultCode", "404");
                notFoundResult.put("message", "고객을 찾을 수 없습니다");
                notFoundResult.put("customerId", customerId);
                return notFoundResult;
            }
            
            // 2. 접근 로그 기록 (vm0001.c의 insert_access_log 함수 역할)
            Map<String, Object> logData = new HashMap<>();
            logData.put("customerId", customerId);
            logData.put("accessTime", LocalDateTime.now());
            logData.put("accessType", "INQUIRY");
            
            dao.insert("vm0001.insertAccessLog", logData);
            
            // 3. 고객 상태 업데이트 (vm0001.c의 update_last_access 함수 역할)
            Map<String, Object> updateData = new HashMap<>();
            updateData.put("customerId", customerId);
            updateData.put("lastAccessTime", LocalDateTime.now());
            
            dao.update("vm0001.updateLastAccess", updateData);
            
            // 4. 결과 반환
            Map<String, Object> result = new HashMap<>();
            result.put("resultCode", "200");
            result.put("message", "고객정보 조회 성공");
            result.put("customerInfo", customer);
            result.put("accessTime", LocalDateTime.now());
            
            log.info("[vm0001] 고객정보 조회 완료 - customerId: {}", customerId);
            return result;
            
        } catch (Exception e) {
            log.error("[vm0001] 고객정보 조회 중 오류 발생", e);
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("resultCode", "500");
            errorResult.put("message", "고객정보 조회 중 오류가 발생했습니다");
            errorResult.put("error", e.getMessage());
            return errorResult;
        }
    }
}