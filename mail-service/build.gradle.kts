
application {
    mainClass = "ApplicationKt"
}

plugins {
    id("io.ktor.plugin") version "3.2.3"
}

dependencies {
    implementation(project(":shared"))

    //Mail
    implementation("org.eclipse.angus:jakarta.mail:2.0.3")

}