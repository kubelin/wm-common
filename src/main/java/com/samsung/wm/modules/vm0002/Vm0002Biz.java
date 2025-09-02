package com.samsung.wm.modules.vm0002;

import com.samsung.common.service.AbstractTypedModuleService;
import com.samsung.common.response.CommonResponse;
import com.samsung.wm.modules.vm0001.dto.CustomerDto;
import com.samsung.wm.modules.vm0002.dao.Vm0002Dao;
import com.samsung.wm.modules.vm0002.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * VM0002 모듈 서비스 (계좌잔고 조회)
 * 
 * C 파일: vm0002.c, vm0002.h  
 * 기능: 고객의 계좌 목록 및 잔고 조회
 * Factory Pattern + Input/Output DTO 지원
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class Vm0002Biz extends AbstractTypedModuleService<Vm0002InputDto, Vm0002OutputDto> {
    
    private final Vm0002Dao vm0002Dao;
    
    @Override
    public String getServiceId() {
        return "vm0002";
    }
    
    @Override
    public String getDescription() {
        return "계좌잔고 조회 서비스";
    }
    
    @Override
    public Class<Vm0002InputDto> getInputDtoClass() {
        return Vm0002InputDto.class;
    }
    
    @Override
    public Class<Vm0002OutputDto> getOutputDtoClass() {
        return Vm0002OutputDto.class;
    }
    
    @Override
    public CommonResponse<Vm0002OutputDto> processTyped(Vm0002InputDto inputDto) {
        String customerId = inputDto.getCustomerId();
        String accountType = inputDto.getAccountType();
        
        log.info("[vm0002] 계좌잔고 조회 시작 - customerId: {}, accountType: {}", customerId, accountType);
        
        try {
            // 1. 고객 존재 확인 (vm0002.c의 check_customer_exists 함수 역할)
            CustomerDto customer = vm0002Dao.checkCustomer(customerId);
            
            if (customer == null) {
                log.warn("[vm0002] 고객을 찾을 수 없습니다 - customerId: {}", customerId);
                Vm0002OutputDto notFoundResult = Vm0002OutputDto.builder()
                    .resultCode("404")
                    .message("고객을 찾을 수 없습니다")
                    .customerId(customerId)
                    .inquiryTime(LocalDateTime.now())
                    .build();
                return CommonResponse.success(notFoundResult, "고객을 찾을 수 없습니다");
            }
            
            // 2. 계좌 목록 조회 (vm0002.c의 select_account_list 함수 역할)
            List<AccountDto> accounts = vm0002Dao.selectAccountList(customerId, accountType);
            
            // 3. 총 잔고 계산 (vm0002.c의 calculate_total_balance 함수 역할)
            BigDecimal totalBalance = accounts.stream()
                .map(AccountDto::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            // 4. 계좌별 잔고 정보 보강 (vm0002.c의 get_account_detail 함수 역할)
            accounts.forEach(account -> {
                account.setLastTransactionTime(LocalDateTime.now().minusDays(1));
                account.setInterestRate(calculateInterestRate(account.getAccountType()));
            });
            
            // 5. 조회 로그 기록 (vm0002.c의 insert_inquiry_log 함수 역할)
            InquiryLogDto inquiryLog = InquiryLogDto.builder()
                    .customerId(customerId)
                    .inquiryType("BALANCE")
                    .inquiryTime(LocalDateTime.now())
                    .accountCount(accounts.size())
                    .build();
            vm0002Dao.insertInquiryLog(inquiryLog);
            
            // 6. 결과 반환 (타입 안전한 DTO)
            Vm0002OutputDto result = Vm0002OutputDto.builder()
                .resultCode("200")
                .message("계좌잔고 조회 성공")
                .customerId(customerId)
                .accountCount(accounts.size())
                .totalBalance(totalBalance)
                .accounts(accounts)
                .inquiryTime(LocalDateTime.now())
                .build();
            
            log.info("[vm0002] 계좌잔고 조회 완료 - customerId: {}, 계좌수: {}, 총잔고: {}", 
                    customerId, accounts.size(), totalBalance);
            
            return CommonResponse.success(result, "계좌잔고 조회 성공");
            
        } catch (Exception e) {
            log.error("[vm0002] 계좌잔고 조회 중 오류 발생", e);
            Vm0002OutputDto errorResult = Vm0002OutputDto.builder()
                .resultCode("500")
                .message("계좌잔고 조회 중 오류가 발생했습니다: " + e.getMessage())
                .customerId(customerId)
                .inquiryTime(LocalDateTime.now())
                .build();
            return CommonResponse.success(errorResult, "처리 중 오류 발생");
        }
    }
    
    /**
     * 계좌 유형별 이자율 계산 (vm0002.c의 get_interest_rate 함수 역할)
     */
    private double calculateInterestRate(String accountType) {
        return switch (accountType) {
            case "SAVINGS" -> 2.5;
            case "CHECKING" -> 0.5;
            case "INVESTMENT" -> 4.0;
            default -> 1.0;
        };
    }
}