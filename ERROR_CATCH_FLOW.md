# 모듈 에러 캐치 플로우

## 🔄 전체 실행 플로우

### 1. 정상적인 API 호출
```bash
POST /api/module/vm0001
{
  "customerId": "CUST000001"  
}
```

### 2. Factory 패턴으로 모듈 선택
```java
// CommonModuleController.java
ModuleService service = factory.getService("vm0001");  // → Vm0001Biz 반환
CommonResponse<?> response = service.process(input);
```

### 3. AbstractTypedModuleService에서 실행
```java
// AbstractTypedModuleService.java
@Override
public CommonResponse<?> process(Map<String, Object> input) {
    String serviceId = getServiceId();  // "vm0001"
    
    try {
        // 1. DTO 변환
        I inputDto = convertToDto(input, getInputDtoClass());
        
        // 2. 실제 비즈니스 로직 실행 ← 여기서 에러 발생 가능!
        O result = processTyped(inputDto);
        
        // 3. 성공 응답
        return CommonResponse.success(result, serviceId + " 처리 완료");
        
    } catch (Exception e) {
        // 🎯 모든 에러가 여기서 캐치됨!
        return handleError(serviceId, e, input);
    }
}
```

## 🚨 에러 캐치 시나리오

### 시나리오 1: 입력 검증 에러
```java
// Vm0001Biz.processTyped() → validateInput()
if (customerId.length() != 10) {
    throw new IllegalArgumentException("고객ID는 10자리여야 합니다");
    //     ⬇️
    //     AbstractTypedModuleService.process()의 catch 블록에서 캐치
    //     ⬇️  
    //     handleError() 호출
    //     ⬇️
    //     ModuleErrorHandler.handleException()에서 처리
    //     ⬇️
    //     IllegalArgumentException → ModuleException.inputValidationError()
    //     ⬇️
    //     CommonResponse.error("0002", "고객ID는 10자리여야 합니다")
}
```

### 시나리오 2: 비즈니스 로직 에러
```java
// Vm0001Biz.processTyped()
CustomerDto customer = vm0001Dao.selectCustomer(customerId);
if (customer == null) {
    throw ModuleException.businessLogicError("vm0001", 
        ErrorCodes.CUSTOMER_NOT_FOUND, "고객을 찾을 수 없습니다", inputDto);
    //     ⬇️
    //     AbstractTypedModuleService.process()의 catch 블록에서 캐치
    //     ⬇️
    //     handleError() 호출
    //     ⬇️
    //     ModuleException이므로 그대로 사용
    //     ⬇️  
    //     CommonResponse.error("6001", "고객을 찾을 수 없습니다")
}
```

### 시나리오 3: 데이터베이스 에러
```java
// Vm0001Biz.processTyped()
try {
    CustomerDto customer = vm0001Dao.selectCustomer(customerId);
} catch (SQLException sqlEx) {
    // SQLException이 발생하면...
    //     ⬇️
    //     AbstractTypedModuleService.process()의 catch 블록에서 캐치
    //     ⬇️
    //     handleError() 호출
    //     ⬇️
    //     ModuleErrorHandler.handleException()에서 처리
    //     ⬇️
    //     isDatabaseError() 체크 → true
    //     ⬇️
    //     ModuleException.databaseError() 생성 (CRITICAL 레벨)
    //     ⬇️
    //     ModuleErrorHandler.logError()에서 🚨 CRITICAL 로그
    //     ⬇️
    //     CommonResponse.error("7001", "데이터베이스 에러")
}
```

## 🔧 handleError() 메서드 상세 동작

```java
// AbstractTypedModuleService.java
private CommonResponse<?> handleError(String serviceId, Exception e, Object inputData) {
    if (errorHandler != null) {
        // 1. 에러 분류 및 변환
        ModuleException moduleEx = errorHandler.handleException(serviceId, e, inputData);
        
        // 2. 심각도에 따른 로깅
        errorHandler.logError(serviceId, moduleEx);
        // INFO  → log.info()
        // WARN  → log.warn()  
        // ERROR → log.error()
        // CRITICAL → log.error("🚨 CRITICAL: ...")
        
        // 3. 적절한 에러코드 결정
        String errorCode = errorHandler.getErrorCode(moduleEx);
        
        // 4. 응답 생성
        return CommonResponse.error(errorCode, moduleEx.getMessage());
        
    } else {
        // Fallback: ErrorHandler가 없으면 기본 처리
        if (e instanceof IllegalArgumentException) {
            return CommonResponse.error(ErrorCodes.INVALID_PARAMETER, e.getMessage());
        } else {
            return CommonResponse.error(ErrorCodes.UNKNOWN_ERROR, "처리 중 오류 발생");
        }
    }
}
```

## 📊 최종 API 응답 예시

### ✅ 성공 케이스
```json
{
  "success": true,
  "code": "0000", 
  "message": "vm0001 처리 완료",
  "data": {
    "resultCode": "200",
    "customerInfo": {...},
    "accessTime": "2024-01-01T10:00:00"
  }
}
```

### ❌ 입력 검증 에러
```json
{
  "success": false,
  "code": "0002",
  "message": "고객ID는 10자리여야 합니다 (현재: 5자리)",
  "data": null
}
```

### ❌ 비즈니스 에러  
```json
{
  "success": false,
  "code": "6001",
  "message": "고객을 찾을 수 없습니다: NOTFOUND01", 
  "data": null
}
```

### ❌ 시스템 에러
```json
{
  "success": false,
  "code": "7001",
  "message": "데이터베이스 연결 실패",
  "data": null
}
```

## 🎯 핵심 포인트

1. **모든 에러는 AbstractTypedModuleService.process()에서 캐치됨**
2. **ModuleErrorHandler가 자동으로 에러 분류**
3. **ErrorCodes 상수가 실제로 활용됨** 
4. **심각도에 따른 자동 로깅**
5. **일관된 API 응답 형식**

모듈 개발자는 그냥 Exception을 던지기만 하면, 나머지는 자동으로 처리됩니다!