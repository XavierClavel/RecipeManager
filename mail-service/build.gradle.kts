val kotlin_version: String by project
val logback_version: String by project

plugins {
    kotlin("jvm")
    kotlin("kapt")
    id("io.ktor.plugin") version "3.2.3"
    id("io.ebean") version "15.8.0"
}

group = "eu.cooknco"
version = "1.0.0"

application {
    mainClass = "ApplicationKt"
}

dependencies {
    val ebeanVersion = "16.0.1"

    implementation(project(":shared"))

    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-netty")
    implementation("io.ktor:ktor-server-core")
    implementation("io.ktor:ktor-server-config-yaml")
    testImplementation("io.ktor:ktor-server-test-host")

    implementation("ch.qos.logback:logback-classic:$logback_version")

    //Mail
    implementation("org.eclipse.angus:jakarta.mail:2.0.3")

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

    //Logging
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")

}

task("prepareKotlinBuildScriptModel") {

}

ebean {
    debugLevel = 1
    queryBeans = true
}