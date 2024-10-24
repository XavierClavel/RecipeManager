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
class Circle (

    @Id
    @GeneratedValue
    var id: Long = -1,

    var title: String = "",

    @ManyToMany
    var recipes: Set<Recipe> = setOf(),

    @ManyToMany
    var users: Set<CircleUser> = setOf(),

    @ManyToMany
    var pendingUsers: Set<User> = setOf(),

    var isPublic: Boolean = false,
)