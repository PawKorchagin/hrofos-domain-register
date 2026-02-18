FROM gradle:8.5-jdk17 AS build
WORKDIR /app

ENV GRADLE_OPTS${DB_USER:***REMOVED***}"-Dhttps.protocols${DB_USER:***REMOVED***}TLSv1.2,TLSv1.3 -Dorg.gradle.jvmargs${DB_USER:***REMOVED***}-Xmx1g"
COPY build.gradle.kts settings.gradle.kts ./
COPY api-gateway/build.gradle.kts ./api-gateway/
COPY common/build.gradle.kts ./common/

COPY api-gateway/src ./api-gateway/src
COPY common/src ./common/src

RUN gradle :api-gateway:bootJar --no-daemon

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

RUN groupadd -r spring && useradd -r -g spring spring
USER spring:spring

COPY --from${DB_USER:***REMOVED***}build /app/api-gateway/build/libs/api-gateway.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
