plugins {
    id("java")
    id("org.springframework.boot") version "3.2.0" apply false
    id("io.spring.dependency-management") version "1.1.4" apply false
}

allprojects {
    group ${DB_USER:***REMOVED***} "ru.itmo.domain-registrar"
    version ${DB_USER:***REMOVED***} "1.0.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin ${DB_USER:***REMOVED***} "java")
    apply(plugin ${DB_USER:***REMOVED***} "io.spring.dependency-management")

    java {
        sourceCompatibility ${DB_USER:***REMOVED***} JavaVersion.VERSION_17
        targetCompatibility ${DB_USER:***REMOVED***} JavaVersion.VERSION_17
    }

    tasks.withType<JavaCompile> {
        options.encoding ${DB_USER:***REMOVED***} "UTF-8"
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}
