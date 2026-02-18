FROM gradle:8.5-jdk17 AS build
WORKDIR /app

ENV GRADLE_OPTS${DB_USER:***REMOVED***}"-Dhttps.protocols${DB_USER:***REMOVED***}TLSv1.2,TLSv1.3 -Dorg.gradle.jvmargs${DB_USER:***REMOVED***}-Xmx1g"

COPY build.gradle.kts settings.gradle.kts ./
COPY audit-service/build.gradle.kts ./audit-service/
COPY common/build.gradle.kts ./common/

COPY audit-service/src ./audit-service/src
COPY common/src ./common/src

RUN gradle :audit-service:clean :audit-service:bootJar --no-daemon --no-build-cache

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

RUN groupadd -r spring && useradd -r -g spring spring
USER spring:spring

COPY --from${DB_USER:***REMOVED***}build /app/audit-service/build/libs/audit-service.jar app.jar

EXPOSE 8087

ENTRYPOINT ["java", "-jar", "app.jar"]
