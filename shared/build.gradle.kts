plugins {
    kotlin("jvm")
    id("org.jetbrains.kotlin.plugin.serialization")
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.1")
    implementation("org.jetbrains.kotlin:kotlin-stdlib")

    //Kafka
    implementation("org.apache.kafka:kafka-clients:4.1.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")
}

task<Wrapper>("wrapper"){
    gradleVersion = "8.6"
}

task("prepareKotlinBuildScriptModel") {

}