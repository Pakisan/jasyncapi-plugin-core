plugins {
    java
    kotlin("jvm") version "1.5.10"
    id("org.jetbrains.kotlinx.kover") version "0.5.0"
    `maven-publish`
    signing
}

group = "com.asyncapi"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    /*
        Junit.
     */
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.8.2")
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
        mavenLocal()
        mavenCentral()
    }

    dependencies {
        classpath("org.jetbrains.kotlinx:kover:0.5.0")
    }
}

apply(plugin = "kover")

// Publishing: Maven
publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group.toString()
            artifactId = rootProject.name
            version = project.version.toString()

            from(components["kotlin"])
            pom {
                name.set("AsyncAPI: common logic for plugins.")
                inceptionYear.set("2020")
                url.set("https://github.com/Pakisan/jasyncapi-plugin-core")
                description.set("""
                    Common logic for plugins.
                """.trimIndent())
                organization {
                    name.set("AsyncAPI Initiative")
                    url.set("https://www.asyncapi.com/")
                }
                developers {
                    developer {
                        name.set("Pavel Bodiachevskii")
                        url.set("https://github.com/Pakisan")
                    }
                }
                licenses {
                    license {
                        name.set("Apache License, Version 2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                        distribution.set("repo")
                        comments.set("A business-friendly OSS license")
                    }
                }
                scm {
                    url.set("https://github.com/Pakisan/jasyncapi-plugin-core")
                    connection.set("scm:git:https://github.com/Pakisan/jasyncapi-plugin-core.git")
                    tag.set("HEAD")
                }
                packaging = "jar"
            }
        }
    }
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/Pakisan/jasyncapi-plugin-core")
            credentials {
                username = project.findProperty("github.packages.user") as String? ?: System.getenv("USERNAME")
                password = project.findProperty("github.packages.key") as String? ?: System.getenv("TOKEN")
            }
        }
    }
}

signing {
    sign(publishing.publications["maven"])
}