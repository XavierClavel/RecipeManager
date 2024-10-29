package com.xavierclavel.utils

import io.ktor.server.auth.authenticate
import io.ktor.server.routing.Route
import io.ktor.server.routing.route

abstract class Controller(val base: String = "") {
    fun serve(route: Route) = route.run {
        route(base) {
            routes()
        }
    }

    abstract fun Route.routes()

    infix fun Route.serve(controller: Controller) = controller.serve(this)
}

abstract class AuthenticatedController(val base: String) {
    fun serve(route: Route) = route.run {
        authenticate("auth-session") {
            route(base) {
                routes()
            }
        }
    }

    abstract fun Route.routes()

    infix fun Route.serve(controller: Controller) = controller.serve(this)
}

fun Route.use(vararg controller: Controller) {
    controller.forEach {it.serve(this) }
}

fun Route.use(vararg controller: AuthenticatedController) {
    controller.forEach {it.serve(this) }
}