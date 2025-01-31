package com.xavierclavel.controllers

import com.xavierclavel.exceptions.AuthenticationException
import com.xavierclavel.services.FollowService
import com.xavierclavel.services.UserService
import com.xavierclavel.utils.Controller
import com.xavierclavel.utils.getIdPathVariable
import com.xavierclavel.utils.getPaging
import com.xavierclavel.utils.getPathId
import com.xavierclavel.utils.getSessionUserId
import common.utils.URL.FOLLOW_URL
import io.ktor.http.HttpStatusCode
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.NotFoundException
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.RoutingContext
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
        isFollowingUser()
        isFollowedByUser()
    }

    private fun Route.follow() = post("/{id}") {
        val (userId,followerId) = handleFollowRequest()
        if (followService.isFollowing(userId, followerId)) throw BadRequestException("User already followed")
        call.respond(HttpStatusCode.Created, followService.createFollow(userId, followerId))
    }

    private fun Route.unfollow() = delete("/{id}") {
        val (userId,followerId) = handleFollowRequest()
        if (!followService.isFollowing(userId, followerId)) throw BadRequestException("User is not followed")
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

    private fun Route.isFollowedByUser() = get("/{id}/followedBy/{targetId}") {
        val userId = getPathId() ?: return@get call.respond(HttpStatusCode.BadRequest)
        val targetId = getIdPathVariable("targetId") ?: return@get call.respond(HttpStatusCode.BadRequest)
        call.respond(followService.isFollowing(userId, targetId))
    }

    private fun Route.isFollowingUser() = get("/{id}/follows/{targetId}") {
        val userId = getPathId() ?: return@get call.respond(HttpStatusCode.BadRequest)
        val targetId = getIdPathVariable("targetId") ?: return@get call.respond(HttpStatusCode.BadRequest)
        call.respond(followService.isFollowing(targetId, userId))
    }



    private fun RoutingContext.handleFollowRequest(): Pair<Long, Long> {
        val userId = getPathId() ?: throw BadRequestException("No id specified in request")
        userService.getUser(userId) ?: throw NotFoundException("User not found")
        val followerId = getSessionUserId() ?: throw AuthenticationException("No session found for user")
        if(userId == followerId) throw BadRequestException("Cannot follow self")
        return Pair(userId, followerId)
    }


}