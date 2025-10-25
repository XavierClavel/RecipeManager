pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

plugins {
    id("com.android.application") version "8.5.2" apply false
    id("com.android.library") version "8.5.2" apply false
    id("org.jetbrains.kotlin.android") version "2.1.10" apply false
    id("org.jetbrains.kotlin.multiplatform") version "2.1.10" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "2.1.10" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.1.10" apply false
}



rootProject.name = "Cook&co"
include("backend")
include("shared")
include("mail-service")