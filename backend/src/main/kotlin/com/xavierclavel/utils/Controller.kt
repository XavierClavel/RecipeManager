package com.xavierclavel.utils

import io.ktor.server.routing.Route
import io.ktor.server.routing.route

abstract class Controller(val base: String) {
    fun serve(route: Route) = route.run {
        route(base) {
            routes()
        }
    }

    abstract fun Route.routes()

    infix fun Route.serve(controller: Controller) = controller.serve(this)
}

fun Route.use(vararg controller: Controller) {
    controller.forEach {it.serve(this) }
}