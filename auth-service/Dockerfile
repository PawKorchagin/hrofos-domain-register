FROM gradle:8.5-jdk17 AS build
WORKDIR /app

ENV GRADLE_OPTS${DB_USER:***REMOVED***}"-Dhttps.protocols${DB_USER:***REMOVED***}TLSv1.2,TLSv1.3"

COPY build.gradle.kts settings.gradle.kts gradle.properties ./
COPY auth-service/build.gradle.kts ./auth-service/
COPY common/build.gradle.kts ./common/

RUN gradle :auth-service:dependencies --no-daemon

COPY auth-service/src ./auth-service/src
COPY common/src ./common/src

RUN gradle :auth-service:bootJar --no-daemon

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

RUN groupadd -r spring && useradd -r -g spring spring
USER spring:spring

COPY --from${DB_USER:***REMOVED***}build /app/auth-service/build/libs/auth-service.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
