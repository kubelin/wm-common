package com.samsung.wm.modules.vm0002;

import com.samsung.common.service.AbstractModuleService;
import com.samsung.wm.modules.vm0001.dto.CustomerDto;
import com.samsung.wm.modules.vm0002.dao.Vm0002Dao;
import com.samsung.wm.modules.vm0002.dto.AccountDto;
import com.samsung.wm.modules.vm0002.dto.InquiryLogDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * VM0002 모듈 서비스 (계좌잔고 조회)
 * 
 * C 파일: vm0002.c, vm0002.h  
 * 기능: 고객의 계좌 목록 및 잔고 조회
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class Vm0002Biz extends AbstractModuleService {
    
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
    protected void validateInput(Map<String, Object> input) {
        // 고객 ID는 필수
        requireField(input, "customerId");
        requireLength(input, "customerId", 7); // CUST001 형태
        
        // 계좌 유형은 선택사항
        if (input.containsKey("accountType")) {
            String accountType = (String) input.get("accountType");
            if (!List.of("SAVINGS", "CHECKING", "INVESTMENT").contains(accountType)) {
                throw new IllegalArgumentException("올바르지 않은 계좌 유형입니다: " + accountType);
            }
        }
    }
    
    @Override
    protected Object executeBusinessLogic(Map<String, Object> input) {
        String customerId = (String) input.get("customerId");
        String accountType = (String) input.get("accountType");
        
        log.info("[vm0002] 계좌잔고 조회 시작 - customerId: {}, accountType: {}", customerId, accountType);
        
        try {
            // 1. 고객 존재 확인 (vm0002.c의 check_customer_exists 함수 역할)
            CustomerDto customer = vm0002Dao.checkCustomer(customerId);
            
            if (customer == null) {
                log.warn("[vm0002] 고객을 찾을 수 없습니다 - customerId: {}", customerId);
                Map<String, Object> notFoundResult = new HashMap<>();
                notFoundResult.put("resultCode", "404");
                notFoundResult.put("message", "고객을 찾을 수 없습니다");
                notFoundResult.put("customerId", customerId);
                return notFoundResult;
            }
            
            // 2. 계좌 목록 조회 (vm0002.c의 select_account_list 함수 역할)
            List<AccountDto> accounts = vm0002Dao.selectAccountList(customerId, accountType);
            
            // 3. 총 잔고 계산 (vm0002.c의 calculate_total_balance 함수 역할)
            BigDecimal totalBalance = accounts.stream()
                .map(account -> account.getBalance())
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
            
            // 6. 결과 반환
            Map<String, Object> result = new HashMap<>();
            result.put("resultCode", "200");
            result.put("message", "계좌잔고 조회 성공");
            result.put("customerId", customerId);
            result.put("accountCount", accounts.size());
            result.put("totalBalance", totalBalance);
            result.put("accounts", accounts);
            result.put("inquiryTime", LocalDateTime.now());
            
            log.info("[vm0002] 계좌잔고 조회 완료 - customerId: {}, 계좌수: {}, 총잔고: {}", 
                    customerId, accounts.size(), totalBalance);
            
            return result;
            
        } catch (Exception e) {
            log.error("[vm0002] 계좌잔고 조회 중 오류 발생", e);
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("resultCode", "500");
            errorResult.put("message", "계좌잔고 조회 중 오류가 발생했습니다");
            errorResult.put("error", e.getMessage());
            return errorResult;
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