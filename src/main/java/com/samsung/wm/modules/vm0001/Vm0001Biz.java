package com.samsung.wm.modules.vm0001;

import com.samsung.common.service.AbstractTypedModuleService;
import com.samsung.common.response.CommonResponse;
import com.samsung.wm.modules.vm0001.dao.Vm0001Dao;
import com.samsung.wm.modules.vm0001.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * VM0001 모듈 서비스 (고객정보 조회)
 * 
 * C 파일: vm0001.c, vm0001.h
 * 기능: 고객 정보 조회 및 접근 로그 기록
 * Factory Pattern + Input/Output DTO 지원
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class Vm0001Biz extends AbstractTypedModuleService<Vm0001InputDto, Vm0001OutputDto> { 
    
    private final Vm0001Dao vm0001Dao;
    
    @Override
    public String getServiceId() {
        return "vm0001";
    }
    
    @Override
    public String getDescription() {
        return "고객정보 조회 서비스";
    }
    
    @Override
    public Class<Vm0001InputDto> getInputDtoClass() {
        return Vm0001InputDto.class;
    }
    
    @Override
    public Class<Vm0001OutputDto> getOutputDtoClass() {
        return Vm0001OutputDto.class;
    }
    
    @Override
    public Vm0001OutputDto processTyped(Vm0001InputDto inputDto) {
        String customerId = inputDto.getCustomerId();
        log.info("[vm0001] 고객정보 조회 시작 - customerId: {}", customerId);
        
        try {
            // 1. 고객 정보 조회 (vm0001.c의 select_customer 함수 역할)
            CustomerDto customer = vm0001Dao.selectCustomer(customerId);
            
            if (customer == null) {
                log.warn("[vm0001] 고객을 찾을 수 없습니다 - customerId: {}", customerId);
                return Vm0001OutputDto.builder()
                    .resultCode("404")
                    .message("고객을 찾을 수 없습니다")
                    .accessTime(LocalDateTime.now())
                    .build();
            }
            
            // 2. 접근 로그 기록 (vm0001.c의 insert_access_log 함수 역할)
            AccessLogDto accessLog = AccessLogDto.builder()
                    .customerId(customerId)
                    .accessType("INQUIRY")
                    .accessTime(LocalDateTime.now())
                    .build();
            vm0001Dao.insertAccessLog(accessLog);
            
            // 3. 고객 상태 업데이트 (vm0001.c의 update_last_access 함수 역할)
            vm0001Dao.updateLastAccess(customerId, LocalDateTime.now());
            
            // 4. 결과 반환 (타입 안전한 DTO)
            Vm0001OutputDto result = Vm0001OutputDto.builder()
                .resultCode("200")
                .message("고객정보 조회 성공")
                .customerInfo(customer)
                .accessTime(LocalDateTime.now())
                .build();
            
            log.info("[vm0001] 고객정보 조회 완료 - customerId: {}", customerId);
            return result;
            
        } catch (Exception e) {
            log.error("[vm0001] 고객정보 조회 중 오류 발생", e);
            return Vm0001OutputDto.builder()
                .resultCode("500")
                .message("고객정보 조회 중 오류가 발생했습니다: " + e.getMessage())
                .accessTime(LocalDateTime.now())
                .build();
        }
    }
}