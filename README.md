# HealBot

> 증상 기반 질환 예측 및 진료과 추천 의료 서비스 플랫폼

**개발 기간:** 2025.11.01 ~ 2025.12.09  
**팀원:** 김재훈, 남윤우, 신선호, 임동균

---

## 프로젝트 소개

일반인이 질병 증상을 느낄 때 어느 진료과를 방문해야 할지 판단하기 어렵습니다.  
HealBot은 사용자가 입력한 증상을 기반으로 예상 질환과 적절한 진료과를 추천하여 효율적인 병원 방문을 돕는 시스템입니다.

📄 **[발표자료 보기](./docs/2025Healbot.pdf)**

---

## 기술 스택

| 분류 | 기술 |
|------|------|
| Frontend | React, JavaScript |
| Backend | Spring Boot, MyBatis |
| Database | Oracle |
| Storage | Cloudflare R2 |
| API | Kakao Map API, Gemini API, Naver OCR API |
| VCS | GitHub |

---

## 주요 기능

- **증상 기반 질환 예측** - 신체 부위별/유형별 증상 선택(최대 10개), 예상 질환 및 진료과 추천
- **AI 자연어 처리** - Gemini API로 자연어 증상 입력 → 키워드 자동 추출
- **위치 기반 병원 추천** - 카카오맵 API 연동, 인근 병원 마커 표시 및 필터링
- **OCR 영수증 인증 리뷰** - Naver OCR API로 실제 방문 인증 기반 리뷰 시스템
- **소셜 로그인** - 카카오 / 네이버 OAuth2 로그인
- **이메일 인증** - Gmail SMTP 기반 6자리 인증코드 발송 (5분 만료)
- **접속 로그 관리** - Spring Scheduler로 매일 자정 7일 이전 로그 자동 삭제

---

## 🗂 ERD

> 총 13개 테이블 (병원, 증상, 회원, 커뮤니티, 리뷰 등)


---

## 담당 기능 (남윤우)

- **섹션 단위 스크롤 네비게이션** - 휠 이벤트 기반 부드러운 섹션 이동, 스크롤 중복 방지 플래그 처리
- **스크롤 시 헤더 색상 변경** - 섹션 위치 감지 후 CSS 클래스 토글로 UX 개선
- **카카오맵 API 연동** - 병원 마커 표시, 바운드 기반 검색, 필터링 기능 구현
- **건강정보 모달 시스템** - 섹션 기반 메인페이지 구성 및 모달 UI 개발
- **병원 상세 정보 모달** - 병원 클릭 시 상세 정보 표시

---

## 관련 레포지토리

- Frontend: [2025Healbot Frontend](https://github.com/2025Healbot)
- Organization: [https://github.com/2025Healbot](https://github.com/2025Healbot)
