# 팀 프로젝트: 쇼핑몰 ("휠라" 리뉴얼)

이 프로젝트는 원래 3인 팀으로 시작했는데, 한 명이 중간에 나가서 최종 2명(저 포함)이서 완성한 쇼핑몰 리뉴얼 프로젝트입니다

강사님이 크롤링해둔 "휠라(FILA)" 상품 데이터를 주셔서, 이걸 기반으로 만들어 봤습니다 (참고했던 원본 디자인 사이트는... 기억이 안 나네요)

<br>

**프로젝트 링크**

* **GitHub 코드 (제가 작업한 브랜치):** https://github.com/loglostdgn7-rgb/newTeamProject/tree/Youngsoo-deploy
* **최종 배포 사이트 (랜딩 페이지):** https://loglostdgn7-rgb.github.io/#portfolio
* **기능설계서 (Google Sheet):** https://docs.google.com/spreadsheets/d/1mxY445eXhC4RJ0ubt_6IgpYjS9puLOVIoMPuUWXb9ws/edit?pli=1&gid=792173682#gid=792173682

---

**제가 맡았던 역할 (백엔드: 보안/로그인)**

팀 프로젝트였기 때문에, 저는 주로 백엔드에서 **로그인과 보안** 파트를 맡아서 개발했습니다

* **Spring Security 기반 로그인 + OAuth2 구현**
    * Spring Security의 Filter Chain을 커스텀해서 인증/인가를 구현했습니다
    * 사용자가 ID/PW로 로그인하면, Security가 세션을 처리하고 API 요청 시 권한을 검증하는 로직을 맡았습니다
    * **카카오 소셜 로그인 (OAuth2)** 기능을 구현했습니다

*(참고: 테스트를 위해 user1 ~ user4 계정을 DB에 미리 생성해두었습니다. 비밀번호:1)*

---

**사용 기술**

| 구분 | 기술 스택 |
| --- | --- |
| Backend | Spring Boot, Spring Security, OAuth2, MyBatis |
| Frontend | Thymeleaf, JavaScript, HTML/CSS |
| Database | MySQL |
| ETC | Git, GitHub, IntelliJ IDEA |

---

**팀 프로젝트 하면서 느낀 점 (팀원과의 협업)**

"고생한 점"보다는, 이 프로젝트가 저의 "첫 팀 프로젝트"였기 때문에 기술적인 것보다 "협업"에 대해 많이 배웠습니다

**1. 네이밍 규칙의 중요성**

* **경험:** 프로젝트를 진행하면서 팀원 간의 '네이밍 규칙(naming convention)'을 초기에 통일하는 게 얼마나 중요한지 깨달았습니다
* **배운 점:** 규칙이 서로 다르면 나중에 코드를 찾거나 읽을 때 비효율이 생긴다는 걸 직접 경험했습니다 이 경험으로, 다음 프로젝트에서는 꼭 초반에 '코딩 컨벤션'을 확실히 정하고 시작해야겠다고 생각했습니다

**2. DTO 같이 쓸 때의 조심스러움**

* **문제 현상:** 저랑 팀원이랑 같이 써야 하는 DTO나 Service 같은 공용 파일이 있었습니다
* **고민:** 근데 제가 거기에 "필요한 필드를 하나 추가"해야 할 때, 혹시나 팀원 코드가 에러날까 봐... 굉장히 조심스럽고 무서웠습니다
* **결론:** 결국 추가하긴 했는데... "어째어째 어케든 돌아간다는 느낌"으로 한 거 같아서, 협업할 때 공용 파일을 수정하는 게 얼마나 신중해야 하는 일인지 알게 됐습니다

---

**로컬에서 실행하기 (간략)**

1. 이 리포지토리를 클론합니다
2. `application.properties`에 MySQL DB 연결 정보를 넣습니다
3. 프로젝트를 실행합니다
