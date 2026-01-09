# Multi-stage build для оптимизации размера образа

# Stage 1: Build
FROM gradle:8.5-jdk17 AS build
WORKDIR /app

# Копируем файлы Gradle
COPY build.gradle.kts settings.gradle.kts ./
COPY api-gateway/build.gradle.kts ./api-gateway/
COPY common/build.gradle.kts ./common/

# Копируем исходный код
COPY api-gateway/src ./api-gateway/src
COPY common/src ./common/src

# Собираем проект
RUN gradle :api-gateway:bootJar --no-daemon

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

RUN groupadd -r spring && useradd -r -g spring spring
USER spring:spring

COPY --from${DB_USER:***REMOVED***}build /app/api-gateway/build/libs/api-gateway.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
