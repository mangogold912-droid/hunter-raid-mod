# 헌터키우기 (Hunter Raid) 모딩 이식 프로젝트
## By DeathLantern

## 프로젝트 개요
- **소스 버전**: v2.6.9 (모딩됨)
- **타겟 버전**: v2.7.1 (최신 - 2026-05-27)
- **엔진**: Unity IL2CPP
- **핵심 파일**: libil2cpp.so, global-metadata.dat

## 모딩 이식 워크플로우

### Step 1: 구조 분석 (Structural Analysis)
- Unity IL2CPP 엔진 사용
- libil2cpp.so와 global-metadata.dat의 구조적 변화 분석
- v2.6.9에서 수정된 주요 지점(오프셋, 함수명)이 최신 버전에서 유지 여부 확인

### Step 2: 코드 추출 및 역공학 (Decompilation & Extraction)
- Dump.cs 또는 script.py 형태의 기존 수정 사항 분석
- 함수의 시그니처(Signature)를 기반으로 최신 버전의 대응 지점 탐색

### Step 3: 이식 전략 수립 (Porting Strategy)
- Case A: 함수 구조 동일 → 오프셋 갱신 및 패치 코드 작성
- Case B: 함수 인라인화/구조 변경 → 대체 후킹 지점 탐색  
- Case C: 보안 로직 강화 → 우회 로직 설계

### Step 4: 구현 및 검증 (Implementation & Validation)
- 런타임 에러(Crash) 방지 정적 분석
- ARM64 어셈블리 레벨 명령어 치환 확인

## 진행 상태
- [x] PDF 프롬프트 분석
- [ ] APK 다운로드 (2.6.9 + 2.7.1)
- [ ] 디컴파일
- [ ] 모딩 코드 추출
- [ ] 이식
- [ ] 빌드
