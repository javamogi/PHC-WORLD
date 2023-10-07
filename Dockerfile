FROM openjdk:8-jdk-alpine AS builder

ENV TZ=Asia/Seoul
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# gradle 설정
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src
RUN chmod +x ./gradlew
#./gradlew Not Found오류 해결
RUN dos2unix ./gradlew
RUN ./gradlew bootJar

FROM openjdk:8-jdk-alpine
COPY --from=builder build/libs/*.jar app.jar

#COPY build/libs/*.jar app.jar

ARG ENVIRONMENT
ENV SPRING_PROFILES_ACTIVE=${ENVIRONMENT}

ENTRYPOINT ["java","-jar","/app.jar"]

#빌드 명령어 docker build --build-arg ENVIRONMENT=h2 -t PHC-WORLD .