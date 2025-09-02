

plugins {
    val kotlinVersion = "2.1.21"
    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.jooq.jooq-codegen-gradle") version "3.20.1"
    id("org.openapi.generator") version "7.13.0"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.23"
    id("com.diffplug.spotless") version "7.2.1"
    kotlin("plugin.spring") version kotlinVersion
    kotlin("jvm") version kotlinVersion
}

group = "tech.pacifici.patronus"
version = "0.0.1-SNAPSHOT"

val jooqVersion by extra("3.20.1")

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.liquibase:liquibase-core")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("com.google.code.gson:gson:2.11.0")
    implementation("io.gsonfire:gson-fire:1.8.5")

    api("org.jooq:jooq:$jooqVersion")

    jooqCodegen("jakarta.xml.bind:jakarta.xml.bind-api:4.0.1")
    jooqCodegen("org.jooq:jooq-meta:$jooqVersion")
    jooqCodegen("org.jooq:jooq-meta-extensions:$jooqVersion")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("io.swagger.core.v3:swagger-core:2.2.15")
    implementation("io.swagger.core.v3:swagger-models:2.2.15")

    implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")
    implementation("ch.qos.logback:logback-classic:1.4.14")

    runtimeOnly("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.3.1")
}

spotless {
    kotlin {
        target("**/*.kt")
        targetExclude("**/build/**/*.kt")
        ktlint()
    }
    kotlinGradle {
        target("*.gradle.kts")
        ktlint()
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

sourceSets {
    val main by getting {
        java {
            srcDir("build/generated-sources/jooq")
        }
        kotlin {
            srcDirs("build/generated-sources/openapi")
        }
    }
}

jooq {
    configuration {
        generator {
            name = "org.jooq.codegen.KotlinGenerator"
            database {
                name = "org.jooq.meta.extensions.ddl.DDLDatabase"
                properties {
                    property {
                        key = "scripts"
                        value = "src/main/resources/db/migration/V1__Create_social_network_tables.sql"
                    }
                    property {
                        key = "defaultNameCase"
                        value = "as-is"
                    }
                }
            }
            target {
                directory = "build/generated-sources/jooq"
                packageName = "tech.pacifici.patronus.model"
            }
            kotlin {
            }
        }
    }
}

val configValues =
    mapOf(
        "interfaceOnly" to "true",
        "dateLibrary" to "java8",
        "gradleBuildFile" to "false",
        "exceptionHandler" to "false",
        "useJakartaEe" to "true",
        "useSpringBoot3" to "true",
        "useTags" to "true",
    )

openApiGenerate {
    generatorName.set("kotlin-spring")
    inputSpec.set("$projectDir/src/main/resources/openapi/patronus-api.yaml")
    outputDir.set("${layout.buildDirectory.get()}/generated-sources/openapi")
    apiPackage.set("tech.pacifici.patronus.api")
    modelPackage.set("tech.pacifici.patronus.model")
    generateApiTests.set(false)
    generateModelTests.set(false)
    generateAliasAsModel.set(false)
    configOptions.set(configValues)
}

tasks.named("compileKotlin") {
    dependsOn(tasks.named("openApiGenerate"))
    dependsOn(tasks.named("jooqCodegen"))
}
