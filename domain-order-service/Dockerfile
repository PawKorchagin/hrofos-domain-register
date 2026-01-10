FROM gradle:8.5-jdk17 AS build
WORKDIR /app

COPY build.gradle.kts settings.gradle.kts gradle.properties ./
COPY domain-order-service/build.gradle.kts ./domain-order-service/
COPY common/build.gradle.kts ./common/

RUN gradle :domain-order-service:dependencies --no-daemon

COPY domain-order-service/src ./domain-order-service/src
COPY common/src ./common/src

RUN gradle :domain-order-service:bootJar --no-daemon

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

RUN groupadd -r spring && useradd -r -g spring spring
USER spring:spring

COPY --from${DB_USER:***REMOVED***}build /app/domain-order-service/build/libs/domain-order-service.jar app.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]
