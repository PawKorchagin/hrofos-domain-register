FROM gradle:8.5-jdk17 AS build
WORKDIR /app

ENV GRADLE_OPTS${DB_USER:***REMOVED***}"-Dhttps.protocols${DB_USER:***REMOVED***}TLSv1.2,TLSv1.3 -Dorg.gradle.jvmargs${DB_USER:***REMOVED***}-Xmx1g"

COPY build.gradle.kts settings.gradle.kts ./
COPY scheduler-service/build.gradle.kts ./scheduler-service/
COPY common/build.gradle.kts ./common/

COPY scheduler-service/src ./scheduler-service/src
COPY common/src ./common/src

RUN gradle :scheduler-service:clean :scheduler-service:bootJar --no-daemon --no-build-cache

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

RUN groupadd -r spring && useradd -r -g spring spring
USER spring:spring

COPY --from${DB_USER:***REMOVED***}build /app/scheduler-service/build/libs/scheduler-service.jar app.jar

EXPOSE 8088

ENTRYPOINT ["java", "-jar", "app.jar"]
