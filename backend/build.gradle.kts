import org.gradle.kotlin.dsl.ebean
import java.util.Properties

val kotlin_version: String by project
val logback_version: String by project
val exposed_version: String by project

plugins {
    //kotlin("kapt") version "2.0.21" apply false
    kotlin("jvm")

    id("io.ktor.plugin") version "3.0.0"
    id("org.jetbrains.kotlin.plugin.serialization")
    id("io.ebean") version "15.8.0"
    id("org.jetbrains.kotlin.kapt")
    id("com.github.johnrengelman.shadow") version "8.1.1"
    //id("com.google.devtools.ksp") version "1.5.30-1.0.0"

}

application {
    mainClass.set("com.xavierclavel.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

dependencies {
    val ktorVersion = "3.1.3"
    val ebeanVersion = "15.8.0"
    val koinVersion = "4.0.0"
    val itextVersion = "8.0.5"
    val testcontainersVersion = "1.20.4"
    val junitVersion = "5.11.4"
    val hopliteVersion = "2.9.0"

    implementation(project(":common"))

    implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")

    //Server -> Ktor
    implementation("io.ktor:ktor-server-core-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-webjars-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-cors:$ktorVersion")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-netty-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-sessions:$ktorVersion")
    implementation("io.ktor:ktor-server-auth:$ktorVersion")
    implementation("io.ktor:ktor-server-status-pages:$ktorVersion")

    //Ktor client
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")

    implementation("org.webjars:jquery:3.2.1")
    implementation("io.github.smiley4:ktor-swagger-ui:4.0.0")
    implementation("ch.qos.logback:logback-classic:$logback_version")

    //Secrets
    implementation("io.github.cdimascio:dotenv-kotlin:6.5.0")

    //Configuration
    implementation("com.sksamuel.hoplite:hoplite-core:$hopliteVersion")
    implementation("com.sksamuel.hoplite:hoplite-yaml:$hopliteVersion")

    //Dependency injection -> Koin
    implementation("io.insert-koin:koin-ktor:$koinVersion")
    implementation("io.insert-koin:koin-test-jvm:$koinVersion")


    //DB -> Ebean
    implementation("org.hibernate:hibernate-core:6.6.1.Final")
    implementation("org.postgresql:postgresql:42.7.4")
    implementation("com.zaxxer:HikariCP:6.0.0")
    implementation("org.flywaydb:flyway-core:10.20.0")
    implementation("io.ebean:ebean:$ebeanVersion")
    implementation("io.ebean:ebean-platform-postgres:$ebeanVersion")
    implementation("io.ebean:ebean-ddl-generator:$ebeanVersion")
    implementation("io.ebean:ebean-migration:14.2.0")
    testImplementation("io.ebean:ebean-test:$ebeanVersion")
    kapt("io.ebean:querybean-generator:$ebeanVersion")
    testImplementation("io.ebean:ebean:$ebeanVersion")

    //Encryption -> bcrypt
    implementation("at.favre.lib:bcrypt:0.10.2")

    //Webp support
    implementation("org.sejda.imageio:webp-imageio:0.1.6")

    //Images metadata parsing
    implementation("com.drewnoakes:metadata-extractor:2.19.0")

    //
    implementation("net.coobird:thumbnailator:0.4.20")

    //PDF write
    implementation("com.itextpdf:itext-core:$itextVersion")
    implementation("com.itextpdf:bouncy-castle-adapter:$itextVersion")

    //Mail
    implementation("org.eclipse.angus:jakarta.mail:2.0.3")

    //Mocking
    testImplementation("io.mockk:mockk:1.13.16")

    //Job scheduling
    implementation("dev.inmo:krontab:2.7.1")

    //Redis for session storage
    implementation("io.lettuce:lettuce-core:6.5.3.RELEASE")

    //Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactive:1.10.1")

    //Tests
    testImplementation("io.ktor:ktor-server-test-host-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")

    //Test containers
    testImplementation("org.testcontainers:testcontainers:$testcontainersVersion")
    testImplementation("org.testcontainers:junit-jupiter:$testcontainersVersion") // For JUnit 5
    testImplementation("org.testcontainers:postgresql:$testcontainersVersion")

}

ktor {
    docker {
        localImageName.set("cooknco/backend")
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

tasks.shadowJar {
    archiveBaseName.set("cooknco")
    archiveClassifier.set("")
}

tasks.test {
    useJUnitPlatform()
}

ebean {
    debugLevel = 1
}
