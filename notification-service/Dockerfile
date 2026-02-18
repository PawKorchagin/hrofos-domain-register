FROM gradle:8.5-jdk17 AS build
WORKDIR /app

ENV GRADLE_OPTS${DB_USER:***REMOVED***}"-Dhttps.protocols${DB_USER:***REMOVED***}TLSv1.2,TLSv1.3 -Dorg.gradle.jvmargs${DB_USER:***REMOVED***}-Xmx1g"
COPY build.gradle.kts settings.gradle.kts ./
COPY notification-service/build.gradle.kts ./notification-service/
COPY common/build.gradle.kts ./common/

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
