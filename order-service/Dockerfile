FROM gradle:8.5-jdk17 AS build
WORKDIR /app

ENV GRADLE_OPTS${DB_USER:***REMOVED***}"-Dhttps.protocols${DB_USER:***REMOVED***}TLSv1.2,TLSv1.3 -Dorg.gradle.jvmargs${DB_USER:***REMOVED***}-Xmx1g"

COPY build.gradle.kts settings.gradle.kts ./
COPY order-service/build.gradle.kts ./order-service/
COPY common/build.gradle.kts ./common/

COPY order-service/src ./order-service/src
COPY common/src ./common/src

RUN gradle :order-service:clean :order-service:bootJar --no-daemon --no-build-cache

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

RUN groupadd -r spring && useradd -r -g spring spring
USER spring:spring

COPY --from${DB_USER:***REMOVED***}build /app/order-service/build/libs/order-service.jar app.jar

EXPOSE 8084

ENTRYPOINT ["java", "-jar", "app.jar"]
