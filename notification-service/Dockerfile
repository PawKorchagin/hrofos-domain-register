FROM gradle:8.5-jdk17 AS build
WORKDIR /app

COPY build.gradle.kts settings.gradle.kts gradle.properties ./
COPY notification-service/build.gradle.kts ./notification-service/
COPY common/build.gradle.kts ./common/

RUN gradle :notification-service:dependencies --no-daemon

COPY notification-service/src ./notification-service/src
COPY common/src ./common/src

RUN gradle :notification-service:bootJar --no-daemon

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

RUN groupadd -r spring && useradd -r -g spring spring
USER spring:spring

COPY --from${DB_USER:***REMOVED***}build /app/notification-service/build/libs/notification-service.jar app.jar

EXPOSE 8085

ENTRYPOINT ["java", "-jar", "app.jar"]
