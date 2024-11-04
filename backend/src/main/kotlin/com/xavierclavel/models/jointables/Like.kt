package com.xavierclavel.models.jointables

import com.xavierclavel.models.Recipe
import com.xavierclavel.models.User
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "likes")
class Like (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    var user: User? =  null,

    @ManyToOne
    @JoinColumn(name = "recipe_id", nullable = false)
    var recipe: Recipe? = null,

    var createdAd: LocalDateTime = LocalDateTime.now(),

)