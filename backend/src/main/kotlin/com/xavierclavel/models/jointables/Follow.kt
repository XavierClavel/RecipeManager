package com.xavierclavel.models.jointables

import com.xavierclavel.models.User
import common.infodto.FollowInfo
import io.ebean.Model
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import java.time.Instant
import java.time.LocalDateTime

@Entity
@Table(name = "follows")
class Follow (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @ManyToOne
    var follower: User? =  null,

    @ManyToOne
    var user: User? = null,

    var pending: Boolean = true,

    var followedSince: Long = Instant.now().epochSecond,
): Model() {
    fun toFollowersInfo() = FollowInfo(
        user = this.follower!!.toOverview(),
        followedSince = this.followedSince,
    )

    fun toFollowsInfo() = FollowInfo(
        user = this.user!!.toOverview(),
        followedSince = this.followedSince,
    )
}