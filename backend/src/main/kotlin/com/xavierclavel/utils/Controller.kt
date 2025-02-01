package com.xavierclavel.utils

import common.enums.Sort
import io.ebean.Paging
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.request.receiveMultipart
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.RoutingContext
import io.ktor.server.routing.route
import io.ktor.server.sessions.get
import io.ktor.server.sessions.sessions
import io.ktor.utils.io.jvm.javaio.toInputStream
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

abstract class Controller(val base: String = "") {
    fun serve(route: Route) = route.run {
        route(base) {
            routes()
        }
    }

    abstract fun Route.routes()

    infix fun Route.serve(controller: Controller) = controller.serve(this)
}

fun Route.serve(vararg controller: Controller) {
    controller.forEach {it.serve(this) }
}

fun RoutingContext.getPaging():Paging =
    Paging.of(
        call.request.queryParameters["page"]?.toIntOrNull() ?: 0,
        call.request.queryParameters["size"]?.toIntOrNull() ?: 20
    )

fun RoutingContext.getSort(): Sort =
    Sort.valueOf(call.request.queryParameters["sort"] ?: "NONE")

fun RoutingContext.getSessionUsername(): String? =
    call.sessions.get<UserSession>()?.username

fun RoutingContext.getSessionUserId(): Long? =
    call.sessions.get<UserSession>()?.id

fun RoutingContext.getPathId(): Long = getIdPathVariable("id") ?: throw BadRequestException("Request is missing ID parameter")

fun RoutingContext.getIdPathVariable(value: String): Long? = call.parameters[value]?.toLongOrNull()
fun RoutingContext.getPathVariable(value: String): String? = call.parameters[value]

fun RoutingContext.getIdPathVariableSet(value: String): Set<Long> = call.parameters[value]?.split(",")?.map { it.toLong() }?.toSet() ?: setOf()

fun RoutingContext.getIdQueryParam(value: String): Long? = call.queryParameters[value]?.toLongOrNull()

suspend fun RoutingContext.handleDeletion(deleted: Boolean?) {
    if (deleted == null) call.respond(HttpStatusCode.NotFound)
    else if (!deleted) call.respond(HttpStatusCode.InternalServerError)
    else call.respond(HttpStatusCode.OK)
}

suspend fun RoutingContext.receiveImage(): BufferedImage {
    val multipart = call.receiveMultipart()
    var image: BufferedImage? = null
    multipart.forEachPart { part ->
        when (part) {
            is PartData.FileItem -> {
                image = ImageIO.read(part.provider().toInputStream()) ?: throw BadRequestException("Invalid image file")
            }
            else -> {
                logger.error { "unexpected form data" }
                throw BadRequestException("Unexpected form data")
            }
        }
        part.dispose()
    }
    return image ?: throw BadRequestException("File not received")
}