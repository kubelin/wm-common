package com.samsung.wm.controller;

import com.samsung.common.orchestrator.ModuleChainOrchestrator;
import com.samsung.common.orchestrator.ModuleChainOrchestrator.ChainStep;
import com.samsung.common.orchestrator.ModuleChainOrchestrator.ChainResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 체인 처리 데모 컨트롤러
 * C 함수의 깊은 호출(20~30 depth)을 Event Chain으로 시연
 * 
 * 실제 사용 예시:
 * 고객조회(vm0001) → 계좌조회(vm0002) → 위험도분석(vm0003) → 추천상품(vm0004)
 */
@Slf4j
@RestController
@RequestMapping("/api/chain")
@RequiredArgsConstructor
public class ChainDemoController {
    
    private final ModuleChainOrchestrator orchestrator;
    
    /**
     * 고객 종합 분석 체인 실행
     * C 함수의 깊은 호출 구조를 Event Chain으로 대체
     * 
     * 기존 C 구조 (20~30 depth):
     * customer_analysis() → validate_customer() → get_customer_info() → 
     * check_customer_status() → get_account_list() → validate_accounts() →
     * calculate_total_balance() → assess_risk_level() → get_credit_score() →
     * recommend_products() → ... (계속 깊어짐)
     */
    @PostMapping("/customer-analysis")
    public ResponseEntity<ChainResult> analyzeCustomer(@RequestBody Map<String, Object> request) {
        log.info("고객 종합 분석 체인 실행 시작 - request: {}", request);
        
        // 체인 단계 정의 (C 함수의 호출 순서와 동일)
        List<ChainStep> analysisChain = List.of(
            new ChainStep("vm0001", "고객 정보 조회", true),     // validate_customer() + get_customer_info()
            new ChainStep("vm0002", "계좌 정보 조회", true),     // get_account_list() + calculate_balance()
            new ChainStep("vm0003", "위험도 분석", false),      // assess_risk_level() + get_credit_score() (선택적)
            new ChainStep("vm0004", "추천 상품 조회", false)     // recommend_products() (선택적)
        );
        
        // 체인 실행 (Event-Driven으로 각 단계별 이벤트 발행)
        ChainResult result = orchestrator.executeSequentialChain(analysisChain, request);
        
        log.info("고객 종합 분석 체인 실행 완료 - chainId: {}, success: {}, 시간: {}ms", 
                result.getChainId(), result.isSuccess(), result.getTotalExecutionTime());
        
        return ResponseEntity.ok(result);
    }
    
    /**
     * 대출 심사 체인 실행
     * 복잡한 대출 심사 프로세스를 Event Chain으로 단순화
     */
    @PostMapping("/loan-approval")
    public ResponseEntity<ChainResult> processLoanApproval(@RequestBody Map<String, Object> request) {
        log.info("대출 심사 체인 실행 시작 - request: {}", request);
        
        // 대출 심사 체인 (실제 금융업무의 복잡한 호출 구조를 단순화)
        List<ChainStep> loanChain = List.of(
            new ChainStep("vm0001", "고객 신용 조회", true),
            new ChainStep("vm0002", "소득 검증", true),
            new ChainStep("vm0005", "담보 평가", true),
            new ChainStep("vm0006", "위험도 산정", true),
            new ChainStep("vm0007", "심사 결과 생성", true)
        );
        
        ChainResult result = orchestrator.executeSequentialChain(loanChain, request);
        
        log.info("대출 심사 체인 실행 완료 - chainId: {}, success: {}, 시간: {}ms", 
                result.getChainId(), result.isSuccess(), result.getTotalExecutionTime());
        
        return ResponseEntity.ok(result);
    }
    
    /**
     * 병렬 처리 체인 예시
     * 독립적인 모듈들을 동시 처리 (C에서는 어려운 구조)
     */
    @PostMapping("/parallel-analysis")
    public ResponseEntity<ChainResult> parallelAnalysis(@RequestBody Map<String, Object> request) {
        log.info("병렬 분석 체인 실행 시작 - request: {}", request);
        
        // 병렬 처리 가능한 독립적인 분석 작업들
        List<ChainStep> parallelChain = List.of(
            new ChainStep("vm0001", "고객 기본 정보", false),
            new ChainStep("vm0008", "시장 데이터 분석", false),
            new ChainStep("vm0009", "포트폴리오 분석", false),
            new ChainStep("vm0010", "리스크 분석", false)
        );
        
        ChainResult result = orchestrator.executeParallelChain(parallelChain, request);
        
        log.info("병렬 분석 체인 실행 완료 - chainId: {}, success: {}, 시간: {}ms", 
                result.getChainId(), result.isSuccess(), result.getTotalExecutionTime());
        
        return ResponseEntity.ok(result);
    }
    
    /**
     * 간단한 체인 상태 조회
     */
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getChainStatus() {
        return ResponseEntity.ok(Map.of(
            "message", "Chain Orchestrator is running",
            "features", List.of(
                "Sequential Chain Processing",
                "Event-Driven Architecture", 
                "Deep C Function Call Replacement",
                "Performance Monitoring",
                "Error Recovery"
            ),
            "available_endpoints", List.of(
                "/api/chain/customer-analysis",
                "/api/chain/loan-approval", 
                "/api/chain/parallel-analysis"
            )
        ));
    }
}