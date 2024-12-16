package com.xavierclavel.controllers

import com.xavierclavel.services.FollowService
import com.xavierclavel.services.UserService
import com.xavierclavel.utils.Controller
import com.xavierclavel.utils.getPaging
import com.xavierclavel.utils.getPathId
import com.xavierclavel.utils.getSessionUserId
import common.utils.URL.FOLLOW_URL
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import org.koin.java.KoinJavaComponent.inject

object FollowController: Controller(FOLLOW_URL) {
    val userService : UserService by inject(UserService::class.java)
    val followService: FollowService by inject(FollowService::class.java)

    override fun Route.routes() {
        follow()
        unfollow()
        getFollowers()
        getFollows()
    }

    private fun Route.follow() = post("/{id}") {
        val userId = getPathId() ?: return@post call.respond(HttpStatusCode.BadRequest)
        val user = userService.getUser(userId) ?: return@post call.respond(HttpStatusCode.NotFound)
        val followerId = getSessionUserId() ?: return@post call.respond(HttpStatusCode.Unauthorized)
        if (followService.isFollowing(userId, followerId)) return@post call.respond(HttpStatusCode.BadRequest)
        call.respond(HttpStatusCode.Created, followService.createFollow(userId, followerId))
    }

    private fun Route.unfollow() = delete("/{id}") {
        val userId = getPathId() ?: return@delete call.respond(HttpStatusCode.BadRequest)
        val user = userService.getUser(userId) ?: return@delete call.respond(HttpStatusCode.NotFound)
        val followerId = getSessionUserId() ?: return@delete call.respond(HttpStatusCode.Unauthorized)
        if (!followService.isFollowing(userId, followerId)) return@delete call.respond(HttpStatusCode.BadRequest)
        val result = followService.deleteFollow(userId, followerId) ?: return@delete call.respond(HttpStatusCode.NotFound)
        if (!result) return@delete call.respond(HttpStatusCode.InternalServerError)
        call.respond(HttpStatusCode.OK)
    }

    private fun Route.getFollowers() = get("/{id}/followers") {
        val userId = getPathId() ?: return@get call.respond(HttpStatusCode.BadRequest)
        val user = userService.getUser(userId) ?: return@get call.respond(HttpStatusCode.NotFound)
        val paging = getPaging()
        call.respond(followService.getFollowers(userId, paging))
    }

    private fun Route.getFollows() = get("/{id}/follows") {
        val userId = getPathId() ?: return@get call.respond(HttpStatusCode.BadRequest)
        val user = userService.getUser(userId) ?: return@get call.respond(HttpStatusCode.NotFound)
        val paging = getPaging()
        call.respond(followService.getFollows(userId, paging))
    }


}