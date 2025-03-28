package com.xavierclavel.services

import com.xavierclavel.exceptions.BadRequestCause
import com.xavierclavel.exceptions.BadRequestException
import com.xavierclavel.exceptions.NotFoundCause
import com.xavierclavel.exceptions.NotFoundException
import com.xavierclavel.models.jointables.Follow
import com.xavierclavel.models.jointables.query.QFollow
import com.xavierclavel.utils.DbTransaction.insertAndGet
import com.xavierclavel.utils.DbTransaction.updateAndGet
import common.infodto.FollowInfo
import io.ebean.Paging
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FollowService: KoinComponent {
    val userService: UserService by inject()

    fun countAll() =
        QFollow().findCount()

    fun getEntityByIds(userId: Long, followerId: Long) : Follow =
        QFollow().where().user.id.eq(userId).and().follower.id.eq(followerId).findOne()
            ?: throw NotFoundException(NotFoundCause.FOLLOW_NOT_FOUND)

    fun isFollowing(userId: Long, followerId: Long) =
        QFollow().where()
            .user.id.eq(userId)
            .follower.id.eq(followerId)
            .exists()

    fun getFollow(userId: Long, followerId: Long): Follow =
        QFollow().where()
            .user.id.eq(userId)
            .follower.id.eq(followerId)
            .pending.eq(true)
            .findOne()
            ?: throw BadRequestException(BadRequestCause.NO_FOLLOW_REQUEST)

    fun acceptFollowRequest(userId: Long, followerId: Long) {
        getFollow(userId, followerId).acceptRequest().updateAndGet()
    }

    fun createFollow(userId: Long, followerId: Long) =
        Follow(
            user = userService.getEntityById(userId),
            follower = userService.getEntityById(followerId),
        ).insertAndGet().toFollowsInfo()

    fun deleteFollow(userId: Long, followerId: Long) =
        getEntityByIds(userId, followerId).delete()

    fun getFollowers(userId: Long, paging: Paging) =
        QFollow()
            .where().user.id.eq(userId)
            .setPaging(paging)
            .findList()
            .map { it.toFollowersInfo() }

    fun getFollows(followerId: Long, paging: Paging) =
        QFollow()
            .where().follower.id.eq(followerId)
            .setPaging(paging)
            .findList()
            .map { it.toFollowsInfo() }

}