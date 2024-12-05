package com.xavierclavel.controllers

import com.xavierclavel.services.LikeService
import com.xavierclavel.utils.Controller
import com.xavierclavel.utils.getPaging
import com.xavierclavel.utils.getPathId
import com.xavierclavel.utils.getIdPathVariable
import com.xavierclavel.utils.getSessionUserId
import common.utils.URL.LIKE_URL
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import org.koin.java.KoinJavaComponent.inject

object LikeController: Controller(LIKE_URL) {
    val likeService : LikeService by inject(LikeService::class.java)

    override fun Route.routes() {
        getLikes()
        isLiked()
        countLikes()
        createLike()
        deleteLike()
    }

    private fun Route.getLikes() = get {
        val userId = getIdPathVariable("user")
        val recipeId = getIdPathVariable("recipe")
        val paging = getPaging()
        call.respond(likeService.find(recipeId, userId, paging))
    }

    private fun Route.isLiked() = get("/{id}") {
        val recipeId = getIdPathVariable("id")
        val userId = getSessionUserId()
        call.respond(likeService.exists(recipeId, userId))
    }

    private fun Route.countLikes() = get("/count") {
        val userId = getIdPathVariable("user")
        val recipeId = getIdPathVariable("recipe")
        call.respond(likeService.count(recipeId, userId))
    }

    private fun Route.createLike() = post("/{id}") {
        val recipeId = getPathId() ?: return@post call.respond(HttpStatusCode.BadRequest)
        val userId = getSessionUserId() ?: return@post call.respond(HttpStatusCode.Unauthorized)
        val userCreated = likeService.createLike(recipeId, userId)
        call.respond(HttpStatusCode.Created, userCreated)
    }

    private fun Route.deleteLike() = delete("/{id}") {
        val recipeId = getPathId() ?: return@delete call.respond(HttpStatusCode.BadRequest)
        val userId = getSessionUserId() ?: return@delete call.respond(HttpStatusCode.Unauthorized)
        val result = likeService.deleteLike(recipeId, userId) ?: return@delete call.respond(HttpStatusCode.BadRequest)
        if (!result) return@delete call.respond(HttpStatusCode.BadRequest)
        call.respond(HttpStatusCode.OK)
    }

}