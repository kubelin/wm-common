package com.samsung.wm.integration.messaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 포트폴리오 이벤트 발행자
 */
@Slf4j
@Component
public class PortfolioEventProducer {
    
    /**
     * 포트폴리오 생성 이벤트 발행
     * 
     * @param event 포트폴리오 생성 이벤트
     */
    public void publishPortfolioCreatedEvent(PortfolioEvent event) {
        log.info("포트폴리오 생성 이벤트 발행 - portfolioId: {}", event.getPortfolioId());
        
        // Kafka, RabbitMQ 등을 통한 이벤트 발행 로직
        // 실제 구현에서는 메시지 브로커에 이벤트를 발행
    }
    
    /**
     * 포트폴리오 업데이트 이벤트 발행
     * 
     * @param event 포트폴리오 업데이트 이벤트
     */
    public void publishPortfolioUpdatedEvent(PortfolioEvent event) {
        log.info("포트폴리오 업데이트 이벤트 발행 - portfolioId: {}", event.getPortfolioId());
        
        // 메시지 브로커를 통한 이벤트 발행
    }
    
    /**
     * 리밸런싱 완료 이벤트 발행
     * 
     * @param event 리밸런싱 이벤트
     */
    public void publishRebalancingCompletedEvent(PortfolioEvent event) {
        log.info("리밸런싱 완료 이벤트 발행 - portfolioId: {}", event.getPortfolioId());
        
        // 메시지 브로커를 통한 이벤트 발행
    }
}