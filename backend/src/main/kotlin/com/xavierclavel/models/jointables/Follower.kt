package com.xavierclavel.models.jointables

import com.xavierclavel.models.Recipe
import com.xavierclavel.models.User
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "followers")
class Follower (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @ManyToOne
    var follower: User? =  null,

    @ManyToOne
    var user: User? = null,

    var pending: Boolean = true,

    var followedSince: LocalDateTime = LocalDateTime.now(),
)