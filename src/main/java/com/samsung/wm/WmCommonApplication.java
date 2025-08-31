package com.samsung.wm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import lombok.extern.slf4j.Slf4j;

/**
 * WM Common Standalone Application
 * C 공통 함수를 Java로 변환한 유틸리티 모듈과 전략 패턴 기반 서비스를 제공하는 Spring Boot 애플리케이션
 */
@Slf4j
@SpringBootApplication
public class WmCommonApplication {

    public static void main(String[] args) {
        log.info("WM Common Application 시작");
        SpringApplication.run(WmCommonApplication.class, args);
        log.info("WM Common Application 기동 완료");
    }
}