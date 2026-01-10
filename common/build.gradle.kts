plugins {
    id("java-library")
    id("io.spring.dependency-management")
}

configure<io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension> {
    imports {
        mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2023.0.0")
    }
}

dependencies {
    api("org.springframework.boot:spring-boot-starter")
    api("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    api("org.springframework.boot:spring-boot-starter-validation")
    
    api("io.jsonwebtoken:jjwt-api:0.12.3")
    api("io.jsonwebtoken:jjwt-impl:0.12.3")
    api("io.jsonwebtoken:jjwt-jackson:0.12.3")
    
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
}
