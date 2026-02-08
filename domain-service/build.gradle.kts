plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("org.openapi.generator")
}

configure<io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension> {
    imports {
        mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2023.0.0")
    }
}


openApiGenerate {
    generatorName.set("spring")
    inputSpec.set("${project.projectDir}/src/main/resources/static/openapi.yaml")
    outputDir.set(project.layout.buildDirectory.get().asFile.resolve("generated/openapi").absolutePath)
    apiPackage.set("ru.itmo.domain.generated.api")
    modelPackage.set("ru.itmo.domain.generated.model")
    invokerPackage.set("ru.itmo.domain.generated")
    configOptions.set(
        mapOf(
            "library" to "spring-boot",
            "useSpringBoot3" to "true",
            "useBeanValidation" to "true",
            "openApiNullable" to "false",
            "useTags" to "true",
            "configPackage" to "ru.itmo.domain.generated.config",
            "interfaceOnly" to "true"
        )
    )
}

val fixDiscriminatorType ${DB_USER:***REMOVED***} tasks.register("fixDiscriminatorType") {
    doLast {
        val genDir ${DB_USER:***REMOVED***} project.layout.buildDirectory.get().asFile.resolve("generated/openapi/src/main/java/ru/itmo/domain/generated/model")
        listOf("DnsRecord.java", "DnsRecordResponse.java").forEach { fileName ->
            val file ${DB_USER:***REMOVED***} genDir.resolve(fileName)
            if (file.exists()) {
                file.writeText(file.readText().replace("public String getType()", "public DnsRecordType getType()"))
            }
        }
    }
}
tasks.named("openApiGenerate") { finalizedBy(fixDiscriminatorType) }
tasks.named("compileJava") {
    dependsOn("openApiGenerate")
}

sourceSets["main"].java.srcDir(project.layout.buildDirectory.get().asFile.resolve("generated/openapi/src/main/java").absolutePath)

dependencies {
    implementation("org.springframework.boot:spring-boot")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")
    implementation("org.liquibase:liquibase-core")
    implementation("com.zaxxer:HikariCP")
    implementation(project(":common"))

    runtimeOnly("org.***REMOVED***ql:***REMOVED***ql")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.bootJar {
    archiveFileName.set("domain-service.jar")
    mainClass.set("ru.itmo.domain.DomainServiceApplication")
}
