import java.util.Properties

val kotlin_version: String by project
val logback_version: String by project
val exposed_version: String by project
val h2_version: String by project

plugins {
    kotlin("jvm")
    id("io.ktor.plugin") version "3.0.0"
    id("org.jetbrains.kotlin.plugin.serialization")
}

application {
    mainClass.set("com.xavierclavel.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

dependencies {
    implementation(project(":common"))

    //Server
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-webjars-jvm")
    implementation("org.webjars:jquery:3.2.1")
    implementation("io.github.smiley4:ktor-swagger-ui:2.9.0")
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.insert-koin:koin-ktor:4.0.0")


    //DB
    implementation("org.hibernate:hibernate-core:6.6.1.Final")
    implementation("org.postgresql:postgresql:42.7.4")
    implementation("com.h2database:h2:$h2_version")
    implementation("com.zaxxer:HikariCP:6.0.0")
    implementation("org.flywaydb:flyway-core:10.20.0")


    testImplementation("io.ktor:ktor-server-test-host-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}

ktor {
    docker {
        localImageName.set("recipe-manager-backend")
        imageTag.set(rootProject.version.toString())
    }
}



val generatedVersionDir = "${buildDir}/generated-version"

sourceSets {
    named("main") {
        output.dir(mapOf("builtBy" to "generateVersionProperties"), generatedVersionDir)
    }
}

tasks.register("generateVersionProperties") {
    doLast {
        val propertiesFile = file("$generatedVersionDir/version.properties")
        propertiesFile.parentFile.mkdirs()
        val properties = Properties()
        properties.setProperty("version", rootProject.version.toString())
        propertiesFile.writer().use { properties.store(it, null) }
    }
}

tasks.named("processResources") {
    dependsOn("generateVersionProperties")
}

