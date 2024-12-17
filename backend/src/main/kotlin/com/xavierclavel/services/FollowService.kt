package com.xavierclavel.services

import at.favre.lib.crypto.bcrypt.BCrypt
import com.xavierclavel.models.User
import com.xavierclavel.models.jointables.Follow
import com.xavierclavel.models.jointables.query.QFollow
import com.xavierclavel.models.query.QIngredient
import com.xavierclavel.models.query.QUser
import com.xavierclavel.utils.DbTransaction.insertAndGet
import com.xavierclavel.utils.DbTransaction.updateAndGet
import com.xavierclavel.utils.logger
import common.dto.UserDTO
import common.infodto.UserInfo
import common.enums.UserRole
import common.infodto.IngredientInfo
import io.ebean.Paging
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.time.Instant

class FollowService: KoinComponent {
    val userService: UserService by inject()

    fun countAll() =
        QFollow().findCount()

    fun findEntityByIds(userId: Long, followerId: Long) : Follow? =
        QFollow().where().user.id.eq(userId).and().follower.id.eq(followerId).findOne()

    fun isFollowing(userId: Long, followerId: Long) =
        QFollow().where().user.id.eq(userId).and().follower.id.eq(followerId).exists()

    fun createFollow(userId: Long, followerId: Long) =
        Follow(
            user = userService.findEntityById(userId),
            follower = userService.findEntityById(followerId),
        ).insertAndGet().toFollowsInfo()

    fun deleteFollow(userId: Long, followerId: Long) =
        findEntityByIds(userId, followerId)?.delete()

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