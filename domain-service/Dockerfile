FROM gradle:8.5-jdk17 AS build
WORKDIR /app

ENV GRADLE_OPTS${DB_USER:***REMOVED***}"-Dhttps.protocols${DB_USER:***REMOVED***}TLSv1.2,TLSv1.3"

COPY build.gradle.kts settings.gradle.kts ./
COPY domain-service/build.gradle.kts ./domain-service/
COPY common/build.gradle.kts ./common/

COPY domain-service/src ./domain-service/src
COPY common/src ./common/src

RUN gradle :domain-service:clean :domain-service:bootJar --no-daemon --no-build-cache

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

RUN groupadd -r spring && useradd -r -g spring spring
USER spring:spring

COPY --from${DB_USER:***REMOVED***}build /app/domain-service/build/libs/domain-service.jar app.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]
