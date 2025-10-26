allprojects {
    version = "1.1.0"
    group = "eu.cooknco"
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.plugin.serialization")
}