allprojects {
    version = "1.1.0"
    group = "eu.cooknco"
}

plugins {
    kotlin("jvm") version "2.2.21"
}

val junitVersion = "5.10.2"
val testcontainersVersion = "1.20.1"
val kotlin_version = "2.2.21"

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.plugin.serialization")

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    dependencies {
        // Kotlin test API (works with JUnit 5)
        testImplementation(kotlin("test"))

        // JUnit 5 engine
        //testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
        //testImplementation("org.jetbrains.kotlin:kotlin-test-junit:${kotlin_version}")
        //testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")

        // Testcontainers (for integration tests)
        testImplementation("org.testcontainers:junit-jupiter:$testcontainersVersion")
    }
}

tasks.register("printVersion") {
    doLast {
        println(project.version)
    }
}