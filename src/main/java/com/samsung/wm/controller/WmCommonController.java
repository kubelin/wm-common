package com.samsung.wm.controller;

import com.samsung.common.constants.ErrorCodes;
import com.samsung.common.util.StringUtil;
import com.samsung.common.util.DateUtil;
import com.samsung.common.calc.FinancialCalculator;
import com.samsung.common.converter.DataConverter;
import com.samsung.wm.service.ConsultationService;
import com.samsung.wm.service.InvestmentPlanningService;
import com.samsung.wm.strategy.consultation.ConsultationResult;
import com.samsung.wm.strategy.investment.InvestmentPlan;
import com.samsung.wm.strategy.investment.InvestmentRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * WM Common 기능 테스트 컨트롤러
 * C→Java 변환 유틸리티와 전략 패턴 서비스를 테스트할 수 있는 API 제공
 */
@Slf4j
@RestController
@RequestMapping("/api/wm-common")
@RequiredArgsConstructor
public class WmCommonController {
    
    private final ConsultationService consultationService;
    private final InvestmentPlanningService investmentPlanningService;

    /**
     * Health Check
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", DateUtil.formatDateTime(java.time.LocalDateTime.now()));
        response.put("application", "WM Common Standalone");
        response.put("version", "1.0.0");
        return ResponseEntity.ok(response);
    }

    /**
     * 공통 유틸리티 테스트
     */
    @GetMapping("/utils/test")
    public ResponseEntity<Map<String, Object>> testUtils() {
        Map<String, Object> response = new HashMap<>();
        
        // StringUtil 테스트
        String testString = "   test@example.com   ";
        response.put("string_utils", Map.of(
            "original", testString,
            "is_empty", StringUtil.isEmpty(testString),
            "is_not_empty", StringUtil.isNotEmpty(testString.trim()),
            "masked", StringUtil.mask("1234567890", '*', 3, 7),
            "is_valid_email", StringUtil.isValidEmail("test@example.com")
        ));
        
        // DateUtil 테스트
        LocalDate today = LocalDate.now();
        response.put("date_utils", Map.of(
            "today", DateUtil.formatDate(today),
            "days_until_year_end", DateUtil.daysBetween(today, LocalDate.of(today.getYear(), 12, 31)),
            "age_if_born_1990", DateUtil.calculateAge(LocalDate.of(1990, 1, 1))
        ));
        
        // FinancialCalculator 테스트
        BigDecimal principal = new BigDecimal("1000000");
        BigDecimal rate = new BigDecimal("0.05");
        BigDecimal years = new BigDecimal("5");
        BigDecimal compoundResult = FinancialCalculator.compoundInterest(principal, rate, 1, years);
        
        response.put("financial_calc", Map.of(
            "principal", DataConverter.toCurrencyString(principal) + "원",
            "rate", "5%",
            "years", "5년",
            "compound_result", DataConverter.toCurrencyString(compoundResult) + "원"
        ));
        
        // ErrorCodes 테스트
        response.put("error_codes", Map.of(
            "success", ErrorCodes.SUCCESS,
            "null_parameter", ErrorCodes.NULL_PARAMETER,
            "business_error", ErrorCodes.BUSINESS_RULE_VIOLATION
        ));
        
        return ResponseEntity.ok(response);
    }

    /**
     * 상담 서비스 테스트
     */
    @PostMapping("/consultation")
    public ResponseEntity<Map<String, Object>> testConsultation(
            @RequestParam String customerId,
            @RequestParam(defaultValue = "INITIAL") String consultationType) {
        
        log.info("상담 서비스 테스트 - customerId: {}, type: {}", customerId, consultationType);
        
        ConsultationResult result = consultationService.conductConsultation(customerId, consultationType);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("consultation_result", result);
        response.put("timestamp", DateUtil.formatDateTime(java.time.LocalDateTime.now()));
        
        return ResponseEntity.ok(response);
    }

    /**
     * 투자 계획 서비스 테스트
     */
    @PostMapping("/investment-plan")
    public ResponseEntity<Map<String, Object>> testInvestmentPlan(
            @RequestParam String customerId,
            @RequestParam BigDecimal amount,
            @RequestParam(defaultValue = "conservative") String riskProfile,
            @RequestParam(defaultValue = "medium") String period,
            @RequestParam(defaultValue = "balanced") String goal) {
        
        log.info("투자 계획 서비스 테스트 - customerId: {}, amount: {}", customerId, amount);
        
        InvestmentRequest request = new InvestmentRequest(
            customerId, amount, riskProfile, period, goal, false
        );
        
        InvestmentPlan plan = investmentPlanningService.createInvestmentPlan(request);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("investment_plan", plan);
        response.put("timestamp", DateUtil.formatDateTime(java.time.LocalDateTime.now()));
        
        return ResponseEntity.ok(response);
    }
}