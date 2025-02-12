package com.xavierclavel.utils

import com.xavierclavel.controllers.AuthController.getSessionUserId
import com.xavierclavel.exceptions.BadRequestCause
import com.xavierclavel.exceptions.BadRequestException
import com.xavierclavel.exceptions.ForbiddenCause
import com.xavierclavel.exceptions.ForbiddenException
import common.enums.Sort
import io.ebean.Paging
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.server.request.receiveMultipart
import io.ktor.server.response.header
import io.ktor.server.response.respond
import io.ktor.server.response.respondBytes
import io.ktor.server.routing.Route
import io.ktor.server.routing.RoutingCall
import io.ktor.server.routing.RoutingContext
import io.ktor.server.routing.route
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

fun RoutingContext.getPathId(): Long = getIdPathVariable("id") ?: throw BadRequestException(BadRequestCause.INVALID_REQUEST)

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
                image = ImageIO.read(part.provider().toInputStream()) ?: throw BadRequestException(BadRequestCause.INVALID_IMAGE)
            }
            else -> {
                logger.error { "unexpected form data" }
                throw BadRequestException(BadRequestCause.INVALID_IMAGE)
            }
        }
        part.dispose()
    }
    return image ?: throw BadRequestException(BadRequestCause.INVALID_IMAGE)
}

suspend fun RoutingCall.respondPDF(filename: String, content: ByteArray) {
    response.header("Content-Disposition", "attachment; filename=\"$filename\"")
    respondBytes(content, contentType = ContentType.Application.Pdf)
}

suspend fun RoutingContext.checkUserEditionRights(userId: Long) {
    val currentUser = getSessionUserId()
    if(currentUser == userId) return
    throw ForbiddenException(ForbiddenCause.NOT_ALLOWED_TO_EDIT_USER)
}

suspend fun RoutingContext.checkRecipeEditionRights(recipeOwner: Long) {
    val currentUser = getSessionUserId()
    if (recipeOwner == currentUser) return
    throw ForbiddenException(ForbiddenCause.NOT_ALLOWED_TO_EDIT_RECIPE)
}