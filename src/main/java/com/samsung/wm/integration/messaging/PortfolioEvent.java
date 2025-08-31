package com.samsung.wm.integration.messaging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 포트폴리오 이벤트 클래스
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioEvent {
    
    private String eventId;
    private String portfolioId;
    private String customerId;
    private PortfolioEventType eventType;
    private String description;
    private Map<String, Object> eventData;
    private LocalDateTime occurredAt;
}