package com.samsung.wm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.mybatis.spring.annotation.MapperScan;
import lombok.extern.slf4j.Slf4j;

/**
 * WM Common Standalone Application
 * C 공통 함수를 Java로 변환한 유틸리티 모듈과 전략 패턴 기반 서비스를 제공하는 Spring Boot 애플리케이션
 * 
 * 에러 캐치 시스템 통합:
 * - ModuleException을 통한 정확한 에러 분류
 * - ErrorInterceptor를 통한 자동 에러 캐치 및 통계
 * - ErrorMonitor를 통한 주기적 모니터링 및 알림
 */
@Slf4j
@SpringBootApplication(scanBasePackages = {"com.samsung.wm", "com.samsung.common"})
@MapperScan("com.samsung.wm.modules.**.dao")
@EnableScheduling
public class WmCommonApplication {

    public static void main(String[] args) {
        log.info("WM Common Application 시작");
        SpringApplication.run(WmCommonApplication.class, args);
        log.info("WM Common Application 기동 완료");
    }
}