package com.samsung.wm.modules.vm0001;

import com.samsung.common.constants.ErrorCodes;
import com.samsung.common.exception.ModuleException;
import com.samsung.common.service.AbstractTypedModuleService;
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
    protected Class<Vm0001InputDto> getInputDtoClass() {
        return Vm0001InputDto.class;
    }
    
    @Override
    protected Class<Vm0001OutputDto> getOutputDtoClass() {
        return Vm0001OutputDto.class;
    }
    
    @Override
    public Vm0001OutputDto processTyped(Vm0001InputDto inputDto) {
        // 입력 검증 (자동으로 ModuleErrorHandler가 처리하거나, 직접 처리 가능)
        validateInput(inputDto);
        
        String customerId = inputDto.getCustomerId();
        log.info("[vm0001] 고객정보 조회 시작 - customerId: {}", customerId);
        
        try {
            // 1. 고객 정보 조회 (vm0001.c의 select_customer 함수 역할)
            CustomerDto customer = vm0001Dao.selectCustomer(customerId);
            
            if (customer == null) {
                // 비즈니스 에러: WARN 레벨 (정상적인 비즈니스 로직)
                throw ModuleException.businessLogicError(
                    "vm0001",
                    ErrorCodes.CUSTOMER_NOT_FOUND,
                    "고객을 찾을 수 없습니다: " + customerId,
                    inputDto
                );
            }
            
            // 고객 상태 검증 (비즈니스 규칙)
            if ("SUSPENDED".equals(customer.getStatus())) {
                // 비즈니스 에러: ERROR 레벨 (즉시 처리 필요)
                throw new ModuleException(
                    ErrorCodes.CUSTOMER_SUSPENDED,
                    "vm0001",
                    "정지된 고객입니다: " + customerId,
                    ModuleException.ErrorPhase.BUSINESS_LOGIC,
                    inputDto,
                    false, // 복구 불가능
                    ModuleException.ErrorSeverity.ERROR
                );
            }
            
            // 2. 접근 로그 기록 (vm0001.c의 insert_access_log 함수 역할)
            try {
                AccessLogDto accessLog = AccessLogDto.builder()
                        .customerId(customerId)
                        .accessType("INQUIRY")
                        .accessTime(LocalDateTime.now())
                        .build();
                vm0001Dao.insertAccessLog(accessLog);
            } catch (Exception e) {
                // DB 에러: CRITICAL 레벨 (로그는 중요한 감사 기록)
                throw ModuleException.databaseError(
                    "vm0001", 
                    "접근 로그 기록 실패: " + e.getMessage(), 
                    e, 
                    inputDto
                );
            }
            
            // 3. 고객 상태 업데이트 (vm0001.c의 update_last_access 함수 역할)
            try {
                vm0001Dao.updateLastAccess(customerId, LocalDateTime.now());
            } catch (Exception e) {
                // DB 에러이지만 INFO 레벨 (최종 접근시간 업데이트는 선택적)
                log.warn("[vm0001] 최종 접근시간 업데이트 실패 (계속 진행): {}", e.getMessage());
                // 에러를 던지지 않고 계속 진행
            }
            
            // 4. 결과 반환 (타입 안전한 DTO)
            Vm0001OutputDto result = Vm0001OutputDto.builder()
                .resultCode("200")
                .message("고객정보 조회 성공")
                .customerInfo(customer)
                .accessTime(LocalDateTime.now())
                .build();
            
            log.info("[vm0001] 고객정보 조회 완료 - customerId: {}", customerId);
            return result;
            
        } catch (ModuleException e) {
            // ModuleException은 그대로 던져서 ErrorHandler가 처리하게 함
            throw e;
        } catch (Exception e) {
            // 예상치 못한 에러: CRITICAL 레벨
            throw ModuleException.systemError("vm0001", "시스템 오류: " + e.getMessage(), e);
        }
    }
    
    /**
     * VM0001 입력 데이터 검증
     * 
     * 옵션 1: 기본 IllegalArgumentException (ModuleErrorHandler가 자동 처리)
     * 옵션 2: 직접 ModuleException으로 세밀한 제어
     */
    private void validateInput(Vm0001InputDto inputDto) {
        if (inputDto == null) {
            // 옵션 1: 간단한 방법 (ModuleErrorHandler가 자동으로 WARN 레벨로 처리)
            throw new IllegalArgumentException("입력 데이터가 필요합니다");
        }
        
        String customerId = inputDto.getCustomerId();
        if (customerId == null || customerId.trim().isEmpty()) {
            // 옵션 2: 직접 제어 (필수 입력은 ERROR 레벨)
            throw new ModuleException(
                ErrorCodes.REQUIRED_FIELD_MISSING,
                "vm0001",
                "고객ID는 필수 입력입니다",
                ModuleException.ErrorPhase.INPUT_VALIDATION,
                inputDto,
                true,  // 입력 수정으로 복구 가능
                ModuleException.ErrorSeverity.ERROR
            );
        }
        
        if (customerId.length() != 10) {
            // 옵션 1: 간단한 방법
            throw new IllegalArgumentException("고객ID는 10자리여야 합니다 (현재: " + customerId.length() + "자리)");
        }
        
        if (!customerId.matches("^[A-Z0-9]+$")) {
            // 옵션 2: 보안 관련은 WARN 레벨로 명시적 처리
            throw new ModuleException(
                ErrorCodes.INVALID_FORMAT,
                "vm0001", 
                "고객ID는 영문 대문자와 숫자만 허용됩니다: " + customerId,
                ModuleException.ErrorPhase.INPUT_VALIDATION,
                inputDto,
                true,  // 입력 수정으로 복구 가능
                ModuleException.ErrorSeverity.WARN  // 보안 이슈이므로 모니터링 필요
            );
        }
    }
}