FROM gradle:8.5-jdk17 AS build
WORKDIR /app

ENV GRADLE_OPTS${DB_USER:***REMOVED***}"-Dhttps.protocols${DB_USER:***REMOVED***}TLSv1.2,TLSv1.3"

COPY build.gradle.kts settings.gradle.kts gradle.properties ./
COPY gradle ./gradle
COPY payment-service/build.gradle.kts ./payment-service/
COPY common/build.gradle.kts ./common/

RUN --mount${DB_USER:***REMOVED***}type${DB_USER:***REMOVED***}cache,target${DB_USER:***REMOVED***}/home/gradle/.gradle gradle :payment-service:dependencies --no-daemon

COPY payment-service/src ./payment-service/src
COPY common/src ./common/src

RUN --mount${DB_USER:***REMOVED***}type${DB_USER:***REMOVED***}cache,target${DB_USER:***REMOVED***}/home/gradle/.gradle gradle :payment-service:clean :payment-service:bootJar --no-daemon --no-build-cache

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

RUN groupadd -r spring && useradd -r -g spring spring
USER spring:spring

COPY --from${DB_USER:***REMOVED***}build /app/payment-service/build/libs/payment-service.jar app.jar

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "app.jar"]
