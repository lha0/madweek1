# S-view
## 프로젝트 소개
연락처, 갤러리, 사진정보 총 3개의 탭이 있는 S-view는, 휴대폰 연락처와 갤러리에 쉽고 빠르게 접근할 수 있으며, 세부 검색 및 정보 조회, 수정이 가능한 Android Application 입니다.

### APK file


## 1. 개발 팀원

- 이하영 - UNIST 컴퓨터공학과 20학번
- 양준원 - KAIST 산업공학과 22학번
---
## 2. 개발환경

- Language: Kotlin
- OS: Android

```
minSdkVersion 24
targetSdkVersion 33
```

- IDE: Android Studio
- Target Device:GALAXY S10e
- Version Control: Github
- Design: Figma

---

## 3. 어플리케이션 소개

### TAB 1 - 연락처

<p align="center" width="100%">
    <img src="https://github.com/lha0/madweek1/assets/78598160/ca99b033-d576-4aa9-8daf-e9b57b75e06f" alt="ScreenShot1" width="40%"/>
    <img src="https://github.com/lha0/madweek1/assets/78598160/bd2a5c63-4a4b-4b1e-8968-f77ae6c95dde" alt="ScreenShot2" width="40%"/>
</p>
    

***Major features***

> **보기 좋은 “연락처 추가 및 삭제 뷰어” 창**
> 
- 핸드폰 내부 연락처 정보 띄우기
- 연락처 추가하기
- 연락처 위젯 길게 클릭해 연락처 삭제하기
- 연락처 위젯 짧게 클릭 시 프로필 보기 & 전화 걸기 & 목록으로 돌아가기

---

***기술설명***

- **핸드폰 내부 연락처 정보 가져오기**
    - `ContactsContract API` 를 활용한 핸드폰 연락처 접근 및 서칭
    
- **연락처 목록**
    - `PhoneNumberAdapter`를 활용해 `ListView`에 연락처를 하나씩 띄우기
    
- **연락처 추가하기**
    - `Intent`를 활용해 연락처 추가
    
- **연락처 삭제하기**
    - 연락처 길게 클릭 시, `AlertDialog`를 활용해 연락처 삭제 팝업 메시지 호출 & 삭제하기
    
- **프로필 확인**
    - 연락처 짧게 클릭 시, `fragment transaction`을 활용해 Profile Fragment로 전환
    - 사진, 이름, 전화번호, ‘목록으로’, ‘전화걸기’
    
- **연락처로 전화걸기**
    - `Intent` 를 활용해 전화 다이얼로 연결

---

### TAB 2 - 갤러리

<p align="center" width="100%">
    <img src="https://github.com/lha0/madweek1/assets/78598160/ff80e028-380f-4683-8cd0-f0fc09afd877" alt="ScreenShot3" width="40%"/>
    <img src="https://github.com/lha0/madweek1/assets/78598160/c19d7225-29dd-4241-b148-507ed6ad432c" alt="ScreenShot4" width="40%"/>
</p>

***Major features***

> **깔끔한 기본 이미지 갤러리 뷰어**
> 
- 사진 뷰 전환 스위치 (Recycler 뷰 ↔ 그리드 뷰)
- 핸드폰 내부 이미지 폴더 접근(기본 갤러리 이용) 및 열기 플로팅 버튼
- 촬영 날짜 필터 검색 텍스트 입력창

---

***기술 설명***

- **핸드폰 이미지 접근하기**
    - `mediaStore API` 를 활용한 External Stroage 이미지 ID 접근 및 서칭
    - 글로벌 변수 리스트 (ImageIds, ImageList)를 활용한 사진 정보(URI, id 등) 저장
    
- **각 이미지 띄우기**
    - `ImageAdapter` 로 각 개별 이미지마다 ImageVIew을 만들어 위젯 띄우기
    - `Glide`를 통한 이미지 뷰 최적화
        - 각 개별 이미지 크기 100, 100으로 중앙 부분 잘라서 맞춰 화질 낮춰 빠르게 이미지 불러오기
    
- **플로팅 버튼(+)로 폴더 접근하기**
    - 갤러리 `Intent`를 활용한 이미지 파일 폴더 직접 접근 및 선택
    
- **촬영 날짜 필터 검색**
    - 각 이미지 촬영 날짜 (ImageList의 date)와 editText와의 일치 여부를 통한 필터 검색
    
- **사진 정보 탭 넘어가기**
    - ImageView 클릭 시 arguments로 `bundle` (Id, URI 등)을 넘겨주어 사진 세부 정보 탭으로 넘어가기

---

### TAB 3 - 사진 정보란

![readme5](https://github.com/lha0/madweek1/assets/78598160/cc248618-e5be-42e3-b1bb-75cdd049505b)

***Major features***

> **간결하게 사진 세부 정보 보여주고 수정하기**
> 
- 클릭한 사진의 제목, 날짜, 장소, 카메라 기종 보여주기
- 사진 높은 화질로 크게 보여주기
- 사진 제목 & 위치 클릭 시 내용 수정하기

---

***기술설명***

- **이미지 URI 형식 별 띄우기**
    - 이미지가 drawable 폴더 안에 있는 경우 Int형 주소로 띄우기
    - *imageview.`setImageResource(imageAdress)`*
    - 이미지가 external storage의 URI에 위치한 경우 String 주소를 parsing해 띄우기
    - `*val uri = Uri.parse(imageAdress_gal)*`
    - `*imageView.setImageURI(uri)*`

- **사진 정보 띄우기**
    - 사진 클릭 시 해당 사진의 id를 arguments로 넘겨받음
    - 넘겨받은 id에 해당하는 사진 정보로 각 Textview의 text 설정하기

- **사진 정보 수정하기**
    - 사진 제목 & 위치 클릭 시 해당 textview의 field, 현재 이미지의 ID 전달하기
    - 팝업 텍스트 입력 창에서 입력한 editText 대로 넘겨받은 이미지와 field에 해당하는 사진 정보 변경해서 글로벌 변수 리스트 imageList에 저장하기
