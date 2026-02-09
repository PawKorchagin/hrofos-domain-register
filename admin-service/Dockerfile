FROM gradle:8.5-jdk17 AS build
WORKDIR /app

ENV GRADLE_OPTS${DB_USER:***REMOVED***}"-Dhttps.protocols${DB_USER:***REMOVED***}TLSv1.2,TLSv1.3"

COPY build.gradle.kts settings.gradle.kts ./
COPY admin-service/build.gradle.kts ./admin-service/
COPY common/build.gradle.kts ./common/

COPY admin-service/src ./admin-service/src
COPY common/src ./common/src

RUN gradle :admin-service:clean :admin-service:bootJar --no-daemon --no-build-cache

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

RUN groupadd -r spring && useradd -r -g spring spring
USER spring:spring

COPY --from${DB_USER:***REMOVED***}build /app/admin-service/build/libs/admin-service.jar app.jar

EXPOSE 8086

ENTRYPOINT ["java", "-jar", "app.jar"]
