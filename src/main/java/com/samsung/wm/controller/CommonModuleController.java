package com.samsung.wm.controller;

import com.samsung.common.factory.ModuleServiceFactory;
import com.samsung.common.response.CommonResponse;
import com.samsung.common.service.TypedModuleService;
import com.samsung.wm.modules.vm0001.dto.*;
import com.samsung.wm.modules.vm0002.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 통합 모듈 컨트롤러
 * 모든 C 파일 변환 서비스를 단일 진입점으로 처리
 * Factory Pattern + Input/Output DTO 완전 지원
 */
@Slf4j
@RestController
@RequestMapping("/api/module")
@RequiredArgsConstructor
public class CommonModuleController {
    
    private final ModuleServiceFactory factory;
    
    /**
     * 통합 모듈 처리 엔드포인트
     * 
     * @param serviceId 서비스 ID (예: "vm0001", "vm0002")
     * @param input 입력 데이터
     * @return 처리 결과
     */
    @PostMapping("/{serviceId}")
    public ResponseEntity<CommonResponse<?>> processModule(
            @PathVariable String serviceId,
            @RequestBody Map<String, Object> input) {
        
        log.info("모듈 처리 요청 - serviceId: {}, input: {}", serviceId, input);
        
        try {
            TypedModuleService<?,?> service = factory.getService(serviceId);
            CommonResponse<?> response = service.process(input);
            
            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
            
        } catch (Exception e) {
            log.error("모듈 처리 중 예외 발생 - serviceId: {}", serviceId, e);
            CommonResponse<?> errorResponse = CommonResponse.error(
                "CONTROLLER_ERROR", 
                "컨트롤러에서 오류가 발생했습니다: " + e.getMessage()
            );
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
    
    /**
     * 등록된 모든 서비스 목록 조회
     */
    @GetMapping("/services")
    public ResponseEntity<CommonResponse<Map<String, String>>> getAllServices() {
        log.info("등록된 서비스 목록 조회");
        
        Map<String, String> services = factory.getAllServices();
        CommonResponse<Map<String, String>> response = CommonResponse.success(
            services, 
            "등록된 서비스 " + factory.getServiceCount() + "개"
        );
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 서비스 존재 여부 확인
     */
    @GetMapping("/{serviceId}/exists")
    public ResponseEntity<CommonResponse<Boolean>> checkServiceExists(
            @PathVariable String serviceId) {
        
        log.info("서비스 존재 확인 - serviceId: {}", serviceId);
        
        boolean exists = factory.hasService(serviceId);
        CommonResponse<Boolean> response = CommonResponse.success(
            exists,
            exists ? "서비스가 존재합니다" : "서비스를 찾을 수 없습니다"
        );
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 서비스 상세 정보 조회
     */
    @GetMapping("/{serviceId}/info")
    public ResponseEntity<CommonResponse<Map<String, Object>>> getServiceInfo(
            @PathVariable String serviceId) {
        
        log.info("서비스 상세 정보 조회 - serviceId: {}", serviceId);
        
        if (!factory.hasService(serviceId)) {
            CommonResponse<Map<String, Object>> errorResponse = CommonResponse.error(
                "SERVICE_NOT_FOUND",
                "서비스를 찾을 수 없습니다: " + serviceId
            );
            return ResponseEntity.status(404).body(errorResponse);
        }
        
        TypedModuleService<?,?> service = factory.getService(serviceId);
        Map<String, Object> serviceInfo = Map.of(
            "serviceId", service.getServiceId(),
            "description", service.getDescription(),
            "className", service.getClass().getSimpleName(),
            "supportsTypedDto", service instanceof TypedModuleService
        );
        
        CommonResponse<Map<String, Object>> response = CommonResponse.success(
            serviceInfo,
            "서비스 정보 조회 완료"
        );
        
        return ResponseEntity.ok(response);
    }
    
    // === 타입 안전한 DTO 기반 엔드포인트들 ===
    
    /**
     * VM0001 고객정보 조회 서비스 (타입 안전한 DTO)
     */
    @PostMapping("/vm0001/dto")
    public ResponseEntity<CommonResponse<Vm0001OutputDto>> processVm0001Dto(
            @RequestBody Vm0001InputDto inputDto) {
        
        log.info("VM0001 DTO 처리 요청 - input: {}", inputDto);
        
        try {
            TypedModuleService<?,?> service = factory.getService("vm0001");
            
            // Map으로 변환해서 generic process 호출
            Map<String, Object> inputMap = convertToMap(inputDto);
            
            // 서비스에서 타입 안전하게 DTO를 받아옴
            Vm0001OutputDto result = service.process(inputMap, Vm0001OutputDto.class);
            
            // 컨트롤러에서 CommonResponse로 래핑
            CommonResponse<Vm0001OutputDto> response = CommonResponse.success(result, "VM0001 처리 완료");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("VM0001 DTO 처리 중 예외 발생", e);
            CommonResponse<Vm0001OutputDto> errorResponse = CommonResponse.error(
                "CONTROLLER_ERROR", 
                "VM0001 처리 중 오류가 발생했습니다: " + e.getMessage()
            );
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
    
    /**
     * DTO를 Map으로 변환하는 유틸리티 메소드
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> convertToMap(Object dto) {
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            return mapper.convertValue(dto, Map.class);
        } catch (Exception e) {
            log.error("DTO -> Map 변환 중 오류 발생: {}", e.getMessage());
            throw new IllegalArgumentException("입력 데이터 변환에 실패했습니다: " + e.getMessage());
        }
    }
    
    /**
     * VM0002 계좌잔고 조회 서비스 (타입 안전한 DTO)
     */
    @PostMapping("/vm0002/dto")
    public ResponseEntity<CommonResponse<Vm0002OutputDto>> processVm0002Dto(
            @RequestBody Vm0002InputDto inputDto) {
        
        log.info("VM0002 DTO 처리 요청 - input: {}", inputDto);
        
        try {
            TypedModuleService<?,?> service = factory.getService("vm0002");
            
            // Map으로 변환해서 generic process 호출
            Map<String, Object> inputMap = convertToMap(inputDto);
            
            // 서비스에서 타입 안전하게 DTO를 받아옴
            Vm0002OutputDto result = service.process(inputMap, Vm0002OutputDto.class);
            
            // 컨트롤러에서 CommonResponse로 래핑
            CommonResponse<Vm0002OutputDto> response = CommonResponse.success(result, "VM0002 처리 완료");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("VM0002 DTO 처리 중 예외 발생", e);
            CommonResponse<Vm0002OutputDto> errorResponse = CommonResponse.error(
                "CONTROLLER_ERROR", 
                "VM0002 처리 중 오류가 발생했습니다: " + e.getMessage()
            );
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
}