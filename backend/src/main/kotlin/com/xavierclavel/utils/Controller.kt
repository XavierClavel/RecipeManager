package com.xavierclavel.utils

import io.ebean.Paging
import io.ktor.server.routing.Route
import io.ktor.server.routing.RoutingContext
import io.ktor.server.routing.route
import io.ktor.server.sessions.get
import io.ktor.server.sessions.sessions

abstract class Controller(val base: String = "") {
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

fun RoutingContext.getPaging():Paging =
    Paging.of(
        call.request.queryParameters["page"]?.toIntOrNull() ?: 0,
        call.request.queryParameters["size"]?.toIntOrNull() ?: 20
    )

fun RoutingContext.getSessionUsername(): String? =
    call.sessions.get<UserSession>()?.username
