package com.xavierclavel

import com.xavierclavel.config.appModules
import com.xavierclavel.controllers.AuthController
import com.xavierclavel.controllers.ExportController
import com.xavierclavel.controllers.HealthController
import com.xavierclavel.controllers.IngredientController
import com.xavierclavel.controllers.RecipeController
import com.xavierclavel.controllers.ImageController
import com.xavierclavel.controllers.UserController
import com.xavierclavel.utils.use
import com.xavierclavel.plugins.*
import com.xavierclavel.services.UserService
import com.xavierclavel.utils.logger
import io.ktor.http.HttpHeaders
import io.ktor.server.application.*
import io.ktor.server.auth.authenticate
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.routing.routing
import org.koin.core.context.GlobalContext.startKoin
import org.koin.ktor.ext.inject

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
        allowHeader(HttpHeaders.Authorization)
        allowCredentials = true

    }
    configureAuthentication()
    serveRoutes()

    val userService: UserService by inject()
    userService.setupDefaultAdmin()
}

//Controllers declaration
fun Application.serveRoutes() = routing {
    authenticate("auth-session") {
        use(HealthController)
        use(UserController)
        use(RecipeController)
        use(IngredientController)
        use(ImageController)
        use(ExportController)
    }
    use(AuthController)
}
