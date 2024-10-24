package com.xavierclavel.models

import common.UserRole
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table
class User (

    @Id
    @GeneratedValue
    var id: Long = -1,

    var username: String = "",

    var role: UserRole = UserRole.USER,

    @OneToMany
    var recipes: Set<Recipe> = setOf(),

    @ManyToMany
    var friends: Set<User> = setOf(),

)