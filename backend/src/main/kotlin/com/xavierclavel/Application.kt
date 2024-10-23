package com.xavierclavel

import com.xavierclavel.controllers.HealthController
import com.xavierclavel.utils.use
import com.xavierclavel.plugins.*
import com.xavierclavel.services.RecipeService
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.routing.routing
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin

fun main() {
    startKoin {
        modules(
            services
    )}

    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureSerialization()
    configureDatabases()
    configureRouting()

    serveRoutes()
}

//Controllers declaration
fun Application.serveRoutes() = routing {
    use(HealthController)
}

//Services declaration
val services = module {
    single { RecipeService() }
}