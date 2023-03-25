import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("org.graalvm.buildtools.native") version "0.9.20"
    kotlin("jvm")
    kotlin("plugin.spring")
    kotlin("kapt")
}

group = "xyz.btc.demo"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    maven { url = uri("https://repo.spring.io/release") }
    mavenCentral()
}

dependencies {
    implementation(project(":common"))

    // Spring
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework:spring-jdbc")
    implementation("org.springframework.kafka:spring-kafka")

    // Util
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // Database
    implementation("org.flywaydb:flyway-core")
    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly("org.postgresql:r2dbc-postgresql")

    // Shedlock
    implementation("net.javacrumbs.shedlock:shedlock-spring:5.2.0")
    implementation("net.javacrumbs.shedlock:shedlock-provider-r2dbc:5.2.0")

    // Transport
    implementation("org.apache.kafka:kafka-streams")

    // Kotlin
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(module = "mockito-core")
    }
    testImplementation("com.ninja-squad:springmockk:4.0.2")
}

kotlin { jvmToolchain(17) }

tasks.bootBuildImage {
    builder.set("paketobuildpacks/builder:tiny")
//    imageName.set("")

    environment.set(
        mapOf(
            "BP_NATIVE_IMAGE" to "true",
            "BP_NATIVE_IMAGE_BUILD_ARGUMENTS" to
                """
                    --verbose
                    --no-fallback
                    --initialize-at-build-time=org.slf4j.LoggerFactory,ch.qos.logback
                    --trace-class-initialization=ch.qos.logback.classic.Logger 
                    --initialize-at-run-time=io.netty
                """.trimIndent()
//                    -H:DynamicProxyConfigurationFiles=dynamic-proxy.json
//                -H:DynamicProxyConfigurationResources=dynamic-proxy.json
        )
    )
}


