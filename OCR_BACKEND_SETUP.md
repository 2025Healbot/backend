# OCR 백엔드 구현 완료 ✅

## 구현된 내용

### 1. 의존성 추가 (pom.xml)
- ✅ OkHttp 4.12.0 (HTTP 클라이언트)
- ✅ Jackson Databind (JSON 처리)

### 2. 패키지 구조
```
com.hospital.boot.app.ocr
├── controller
│   └── OcrController.java         # REST API 엔드포인트
├── service
│   └── OcrService.java             # 비즈니스 로직 및 OCR API 호출
└── dto
    ├── OcrRequest.java             # OCR API 요청 DTO
    ├── OcrResponse.java            # OCR API 응답 DTO
    ├── OcrImage.java               # OCR 이미지 결과
    ├── OcrField.java               # OCR 필드 정보
    └── OcrImageInfo.java           # OCR 이미지 정보
```

### 3. API 엔드포인트

#### 영수증 텍스트 추출
- **URL**: `POST /api/ocr/receipt`
- **Content-Type**: `multipart/form-data`
- **Parameters**:
  - `image`: 업로드할 이미지 파일
- **Response**:
```json
{
  "success": true,
  "data": {
    "version": "V2",
    "requestId": "1234567890",
    "timestamp": 1234567890,
    "images": [
      {
        "uid": "image_uid",
        "name": "receipt.jpg",
        "inferResult": "SUCCESS",
        "message": "Success",
        "fields": [
          {
            "valueType": "ALL",
            "inferText": "추출된 텍스트",
            "inferConfidence": 0.9985
          }
        ]
      }
    ]
  }
}
```

#### 서비스 상태 확인
- **URL**: `GET /api/ocr/health`
- **Response**:
```json
{
  "success": true,
  "message": "OCR 서비스가 정상 작동 중입니다.",
  "timestamp": 1234567890
}
```

## 설정 방법

### 1. 네이버 클라우드 플랫폼에서 OCR API 키 발급

1. **네이버 클라우드 플랫폼 접속**
   - https://www.ncloud.com/ 접속
   - 회원가입 및 로그인

2. **OCR 서비스 생성**
   - Console > AI·NAVER API > AI Service
   - CLOVA OCR 선택
   - "이용 신청하기" 클릭

3. **도메인 생성**
   - General OCR 또는 Template OCR 선택 (영수증은 General OCR 권장)
   - 도메인 생성 버튼 클릭
   - 도메인 이름 입력

4. **API 키 확인**
   - 생성된 도메인 선택
   - **APIGW Invoke URL** 복사 (예: `https://xxxx.apigw.ntruss.com/custom/v1/xxxx`)
   - **Secret Key** 복사

### 2. application.properties 설정

`src/main/resources/application.properties` 파일에서 다음 값을 수정:

```properties
# Naver Clover OCR Configuration
naver.ocr.api.url=https://xxxx.apigw.ntruss.com/custom/v1/xxxx/general
naver.ocr.secret.key=YOUR_SECRET_KEY_HERE
```

**주의사항**:
- `naver.ocr.api.url`에는 네이버 클라우드에서 제공한 APIGW Invoke URL을 입력
- URL 마지막에 `/general` 추가 (General OCR 사용 시)
- `naver.ocr.secret.key`에는 Secret Key를 입력
- **절대 Git에 커밋하지 마세요!** (.gitignore 확인)

### 3. Maven 의존성 설치

```bash
# Windows
mvnw.cmd clean install

# Mac/Linux
./mvnw clean install
```

### 4. 애플리케이션 실행

```bash
# Windows
mvnw.cmd spring-boot:run

# Mac/Linux
./mvnw spring-boot:run
```

## 테스트 방법

### 1. Postman으로 테스트

**요청 설정**:
- Method: `POST`
- URL: `http://localhost:8080/api/ocr/receipt`
- Headers:
  - (자동 설정됨)
- Body:
  - Type: `form-data`
  - Key: `image` (File 타입 선택)
  - Value: 영수증 이미지 파일 선택

**예상 응답**:
```json
{
  "success": true,
  "data": {
    "images": [
      {
        "fields": [
          {
            "inferText": "영수증 내용..."
          }
        ]
      }
    ]
  }
}
```

### 2. 프론트엔드와 통합 테스트

1. **백엔드 실행** (기본 포트: 8080)
2. **프론트엔드 실행**
3. 관리자 페이지 `/admin/ocr` 접속
4. 영수증 이미지 업로드 또는 촬영
5. 텍스트 추출 버튼 클릭
6. 결과 확인

### 3. 헬스 체크

```bash
curl http://localhost:8080/api/ocr/health
```

## CORS 설정 (프론트엔드 연동 시)

프론트엔드가 다른 포트에서 실행되는 경우, CORS 설정이 필요합니다.

**방법 1**: Global CORS 설정 (기존 설정 파일 수정)

기존 CORS 설정 파일이 있다면 `/api/ocr/**` 경로를 추가:

```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:3000", "http://localhost:5173")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
```

**방법 2**: Controller 레벨 CORS 설정

`OcrController.java`에 `@CrossOrigin` 추가:

```java
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
@RestController
@RequestMapping("/api/ocr")
public class OcrController {
    // ...
}
```

## 주요 기능

### 1. 파일 검증
- 빈 파일 체크
- 이미지 파일 타입 검증 (image/* 확인)

### 2. 에러 처리
- OCR API 호출 실패 시 상세 에러 메시지 반환
- 로깅을 통한 디버깅 지원

### 3. 타임아웃 설정
- 연결 타임아웃: 30초
- 쓰기 타임아웃: 30초
- 읽기 타임아웃: 30초

## 비용 관련 주의사항

⚠️ **네이버 클로버 OCR은 유료 서비스입니다**

- 무료 크레딧: 월 1,000건
- 초과 시 건당 과금
- 테스트 시 요청 수 제한 권장
- 상용 서비스 전 비용 계산 필요

자세한 요금은 [네이버 클라우드 요금 페이지](https://www.ncloud.com/product/aiService/ocr) 참고

## 트러블슈팅

### 1. "OCR API 호출 실패: 401"
- Secret Key가 올바른지 확인
- application.properties 설정 확인

### 2. "OCR API 호출 실패: 404"
- API URL이 올바른지 확인
- URL 마지막에 `/general` 또는 `/template` 포함 확인

### 3. "이미지 파일만 업로드 가능합니다"
- Content-Type이 image/*인지 확인
- 지원 형식: JPG, PNG, JPEG, BMP, TIFF

### 4. CORS 에러
- 백엔드 CORS 설정 확인
- 프론트엔드 프록시 설정 확인

## 실험 종료 시 되돌리기

이 기능을 제거하려면:

1. **백엔드 파일 삭제**
   - `src/main/java/com/hospital/boot/app/ocr/` 디렉토리 전체 삭제

2. **pom.xml 복원**
   - OkHttp 의존성 제거 (120-125줄)
   - Jackson 의존성 제거 (126-130줄)

3. **application.properties 복원**
   - OCR 설정 제거 (49-53줄)

4. **Maven 재빌드**
   ```bash
   mvnw.cmd clean install
   ```

## 로그 확인

애플리케이션 실행 중 다음과 같은 로그 확인:

```
INFO  : OCR 처리 시작 - 파일명: receipt.jpg, 크기: 123456 bytes
DEBUG : OCR 요청 메시지: {"version":"V2"...}
DEBUG : OCR API 응답: {"version":"V2"...}
INFO  : OCR 처리 완료 - 추출된 이미지 수: 1
INFO  : 영수증 OCR 처리 완료
```

에러 발생 시:
```
ERROR : OCR API 호출 실패 - 상태코드: 401, 응답: ...
ERROR : OCR 처리 중 오류 발생
```

## 다음 단계

1. ✅ 네이버 클라우드에서 OCR API 키 발급
2. ✅ application.properties 설정
3. ✅ 백엔드 실행
4. ✅ 프론트엔드와 통합 테스트
5. 실제 영수증으로 정확도 테스트
6. 필요시 Template OCR로 전환 검토 (영수증 전용 템플릿)
