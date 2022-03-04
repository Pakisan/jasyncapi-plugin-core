plugins {
    java
    kotlin("jvm") version "1.5.10"
    id("org.jetbrains.kotlinx.kover") version "0.5.0"
}

group = "com.asyncapi"
version = "1.0.0-EAP-1"

repositories {
    mavenCentral()
}

dependencies {
    /*
        Junit.
     */
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.12.1")
    implementation("org.reflections:reflections:0.9.12")

    /*
        Kotlin.
     */
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-test")

    /*
        AsyncAPI.
     */
    implementation("com.asyncapi:asyncapi-core:1.0.0-EAP-1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = JavaVersion.VERSION_1_8.toString()
    kotlinOptions.javaParameters = true
    kotlinOptions.allWarningsAsErrors = true
}

java {
    withJavadocJar()
    withSourcesJar()
}

buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath("org.jetbrains.kotlinx:kover:0.5.0")
    }
}

apply(plugin = "kover")