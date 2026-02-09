plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("org.openapi.generator")
}

configure<io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension> {
    imports {
        mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
    }
}

openApiGenerate {
    generatorName.set("spring")
    inputSpec.set("${project.projectDir}/src/main/resources/static/openapi.yaml")
    outputDir.set(project.layout.buildDirectory.get().asFile.resolve("generated/openapi").absolutePath)
    apiPackage.set("ru.itmo.admin.generated.api")
    modelPackage.set("ru.itmo.admin.generated.model")
    invokerPackage.set("ru.itmo.admin.generated")
    configOptions.set(
        mapOf(
            "library" to "spring-boot",
            "useSpringBoot3" to "true",
            "useBeanValidation" to "true",
            "openApiNullable" to "false",
            "useTags" to "true",
            "configPackage" to "ru.itmo.admin.generated.config",
            "interfaceOnly" to "true"
        )
    )
}

tasks.named("compileJava") {
    dependsOn("openApiGenerate")
}

sourceSets["main"].java.srcDir(project.layout.buildDirectory.get().asFile.resolve("generated/openapi/src/main/java").absolutePath)

dependencies {
    implementation("org.springframework.boot:spring-boot")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")
    implementation(project(":common"))

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.bootJar {
    archiveFileName.set("admin-service.jar")
    mainClass.set("ru.itmo.admin.AdminServiceApplication")
}
