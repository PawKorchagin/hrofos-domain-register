pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
    plugins {
        id("org.springframework.boot") version "3.2.0"
        id("io.spring.dependency-management") version "1.1.4"
    }
}

rootProject.name ${DB_USER:***REMOVED***} "domain-registrar"

include("api-gateway", "common", "domain-service", "notification-service", "auth-service", "order-service", "admin-service", "audit-service")
