plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")

    //Kafka
    implementation("org.apache.kafka:kafka-clients:4.1.0")
}