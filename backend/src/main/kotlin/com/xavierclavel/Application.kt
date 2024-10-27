package com.xavierclavel

import com.xavierclavel.config.appModules
import com.xavierclavel.controllers.HealthController
import com.xavierclavel.controllers.UserController
import com.xavierclavel.utils.use
import com.xavierclavel.plugins.*
import com.xavierclavel.services.RecipeService
import com.xavierclavel.services.UserService
import com.xavierclavel.utils.logger
import io.ebean.DatabaseFactory
import io.ktor.http.HttpHeaders
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.routing.routing
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent.inject

fun main() {
    startKoin {
        modules(
            appModules
    )}

    DatabaseManager.init()

    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
    logger.info { " Server started." }
}

fun Application.module() {
    configureSerialization()
    configureRouting()
    install(CORS) {
        anyHost()
        anyMethod()
        allowHeader(HttpHeaders.ContentType)
    }

    serveRoutes()
}

//Controllers declaration
fun Application.serveRoutes() = routing {
    use(HealthController)
    use(UserController)
}
