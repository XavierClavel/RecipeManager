
application {
    mainClass = "ApplicationKt"
}

dependencies {
    implementation(project(":shared"))

    //Mail
    implementation("org.eclipse.angus:jakarta.mail:2.0.3")

}

task("prepareKotlinBuildScriptModel") {

}

ebean {
    debugLevel = 1
    queryBeans = true
}