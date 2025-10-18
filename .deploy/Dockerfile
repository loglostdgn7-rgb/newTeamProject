# 1. Java 21 (JDK 21) 기반 이미지를 사용합니다.
FROM openjdk:21-slim

# 2. 작업 디렉토리를 /app으로 설정
WORKDIR /app

# 3. (중요) Maven 빌드 결과물인 jar 파일의 경로
#    docker-compose.yml의 build context가 ../(프로젝트 루트)이므로,
#    루트 폴더 기준의 target/.. 경로가 맞습니다.
COPY target/project-0.0.1-SNAPSHOT.jar app.jar

# 4. Spring Boot 앱이 사용하는 내부 포트
EXPOSE 8080

# 5. 컨테이너가 시작될 때 이 명령어로 앱을 실행
ENTRYPOINT ["java", "-jar", "app.jar"]