import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.0.5" apply false
    id("io.spring.dependency-management") version "1.0.13.RELEASE" apply false
    kotlin("jvm") version "1.8.10" apply false
    kotlin("plugin.spring") version "1.8.10" apply false
    kotlin("kapt") version "1.8.10" apply false
}
