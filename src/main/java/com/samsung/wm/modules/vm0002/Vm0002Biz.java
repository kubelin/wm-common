package com.samsung.wm.modules.vm0002;

import com.samsung.common.dao.CommonDAO;
import com.samsung.common.service.AbstractModuleService;
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
public class Vm0002Biz extends AbstractModuleService {
    
    public Vm0002Biz(CommonDAO dao) {
        super(dao);
    }
    
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
            Map<String, Object> customer = dao.selectOne(
                "vm0002.checkCustomer",
                Map.of("customerId", customerId),
                Map.class
            );
            
            if (customer == null) {
                log.warn("[vm0002] 고객을 찾을 수 없습니다 - customerId: {}", customerId);
                return Map.of(
                    "resultCode", "404",
                    "message", "고객을 찾을 수 없습니다",
                    "customerId", customerId
                );
            }
            
            // 2. 계좌 목록 조회 (vm0002.c의 select_account_list 함수 역할)
            Map<String, Object> queryParams = new HashMap<>();
            queryParams.put("customerId", customerId);
            if (accountType != null) {
                queryParams.put("accountType", accountType);
            }
            
            List<Map<String, Object>> accounts = dao.selectMapList(
                "vm0002.selectAccountList",
                queryParams
            );
            
            // 3. 총 잔고 계산 (vm0002.c의 calculate_total_balance 함수 역할)
            BigDecimal totalBalance = accounts.stream()
                .map(account -> new BigDecimal(account.get("balance").toString()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            // 4. 계좌별 잔고 정보 보강 (불변 맵을 가변 맵으로 변환)
            List<Map<String, Object>> enrichedAccounts = accounts.stream()
                .map(account -> {
                    Map<String, Object> mutableAccount = new HashMap<>(account);
                    // 계좌별 추가 정보 조회 (vm0002.c의 get_account_detail 함수 역할)
                    String accountNo = (String) mutableAccount.get("accountNo");
                    mutableAccount.put("lastTransactionTime", LocalDateTime.now().minusDays(1));
                    mutableAccount.put("interestRate", calculateInterestRate((String) mutableAccount.get("accountType")));
                    return mutableAccount;
                })
                .toList();
            
            // 5. 조회 로그 기록 (vm0002.c의 insert_inquiry_log 함수 역할)
            Map<String, Object> logData = new HashMap<>();
            logData.put("customerId", customerId);
            logData.put("inquiryType", "BALANCE");
            logData.put("inquiryTime", LocalDateTime.now());
            logData.put("accountCount", enrichedAccounts.size());
            dao.insert("vm0002.insertInquiryLog", logData);
            
            // 6. 결과 반환
            Map<String, Object> result = new HashMap<>();
            result.put("resultCode", "200");
            result.put("message", "계좌잔고 조회 성공");
            result.put("customerId", customerId);
            result.put("accountCount", enrichedAccounts.size());
            result.put("totalBalance", totalBalance);
            result.put("accounts", enrichedAccounts);
            result.put("inquiryTime", LocalDateTime.now());
            
            log.info("[vm0002] 계좌잔고 조회 완료 - customerId: {}, 계좌수: {}, 총잔고: {}", 
                    customerId, enrichedAccounts.size(), totalBalance);
            
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