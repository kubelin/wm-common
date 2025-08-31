package com.samsung.wm.service;

import com.samsung.common.constants.ErrorCodes;
import com.samsung.common.exception.BusinessException;
import com.samsung.common.util.StringUtil;
import com.samsung.wm.strategy.consultation.ConsultationResult;
import com.samsung.wm.strategy.consultation.ConsultationStrategy;
import com.samsung.wm.strategy.consultation.impl.InitialConsultationStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 고객 상담 서비스
 * 단순화된 전략 패턴 적용 (Factory 패턴 제거)
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ConsultationService {
    
    private final InitialConsultationStrategy initialStrategy;
    // 향후 전략 추가시 여기에 주입
    // private final PeriodicConsultationStrategy periodicStrategy;
    // private final EmergencyConsultationStrategy emergencyStrategy;
    
    /**
     * 고객 상담 실행
     * 
     * @param customerId 고객 ID
     * @param consultationType 상담 유형 (INITIAL, PERIODIC, EMERGENCY 등)
     * @return 상담 결과
     */
    public ConsultationResult conductConsultation(String customerId, String consultationType) {
        log.info("고객 상담 실행 - customerId: {}, type: {}", customerId, consultationType);
        
        // 공통 모듈을 활용한 입력 검증
        if (StringUtil.isEmpty(customerId)) {
            throw new BusinessException(ErrorCodes.NULL_PARAMETER, "고객 ID가 필요합니다");
        }
        
        if (StringUtil.isEmpty(consultationType)) {
            throw new BusinessException(ErrorCodes.NULL_PARAMETER, "상담 유형이 필요합니다");
        }
        
        // 간단한 조건문으로 전략 선택 (Factory 패턴 대신)
        ConsultationStrategy strategy = selectStrategy(consultationType);
        
        // 전략 실행
        return strategy.execute(customerId);
    }
    
    /**
     * 상담 유형에 따른 전략 선택
     * 
     * @param consultationType 상담 유형
     * @return 선택된 전략
     */
    private ConsultationStrategy selectStrategy(String consultationType) {
        return switch (consultationType.toUpperCase()) {
            case "INITIAL" -> initialStrategy;
            // case "PERIODIC" -> periodicStrategy;     // 향후 추가
            // case "EMERGENCY" -> emergencyStrategy;   // 향후 추가
            default -> {
                log.warn("지원하지 않는 상담 유형: {} - 기본 전략(INITIAL) 사용", consultationType);
                yield initialStrategy;  // 기본 전략
            }
        };
    }
    
    /**
     * 고객별 상담 이력 조회
     * 
     * @param customerId 고객 ID
     * @return 상담 이력 목록
     */
    public List<ConsultationResult> getConsultationHistory(String customerId) {
        log.info("상담 이력 조회 - customerId: {}", customerId);
        
        // 실제로는 데이터베이스에서 상담 이력을 조회
        return List.of(); // 샘플 구현
    }
}