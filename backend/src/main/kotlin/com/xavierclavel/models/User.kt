package com.xavierclavel.models

import com.xavierclavel.models.jointables.Follower
import com.xavierclavel.models.jointables.Like
import common.enums.UserRole
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "users")
class User (

    @Id
    @GeneratedValue
    var id: Long = -1,

    var username: String = "",

    var role: UserRole = UserRole.USER,

    @OneToMany
    var recipes: Set<Recipe> = setOf(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL])
    var likes : Set<Like> = setOf(),

    @OneToMany(mappedBy = "user")
    var follows: Set<Follower> = setOf(),

    @OneToMany(mappedBy = "follower")
    var followedBy: Set<Follower> = setOf(),

    @ManyToMany
    var circles: Set<Circle> = setOf(),

)