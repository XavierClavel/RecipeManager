package com.xavierclavel.models

import com.xavierclavel.models.jointables.CircleUser
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany
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

    var isPublic: Boolean = false,
)