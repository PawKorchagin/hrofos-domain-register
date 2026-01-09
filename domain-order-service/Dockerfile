FROM gradle:8.5-jdk17 AS build
WORKDIR /app

COPY build.gradle.kts settings.gradle.kts ./
COPY domain-order-service/build.gradle.kts ./domain-order-service/
COPY common/build.gradle.kts ./common/

COPY domain-order-service/src ./domain-order-service/src
COPY common/src ./common/src

RUN gradle :domain-order-service:bootJar --no-daemon \
    --refresh-dependencies \
    -Dorg.gradle.jvmargs${DB_USER:***REMOVED***}"-Djavax.net.ssl.trustStore${DB_USER:***REMOVED***}/etc/ssl/certs/java/cacerts -Dhttps.protocols${DB_USER:***REMOVED***}TLSv1.2,TLSv1.3"

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

RUN groupadd -r spring && useradd -r -g spring spring
USER spring:spring

COPY --from${DB_USER:***REMOVED***}build /app/domain-order-service/build/libs/domain-order-service.jar app.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]
