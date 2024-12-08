package com.xavierclavel.models.jointables

import com.xavierclavel.models.Recipe
import com.xavierclavel.models.User
import common.infodto.LikeInfo
import io.ebean.Model
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.Instant
import java.time.LocalDateTime

@Entity
@Table(name = "likes")
class Like (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @ManyToOne
    var user: User,

    @ManyToOne
    var recipe: Recipe,

    var creationDate: Long = Instant.now().epochSecond,

) : Model() {
    fun toInfo() = LikeInfo(
        user = LikeInfo.User(
            this.user.id,
            this.user.username,
        ),
        recipe = LikeInfo.Recipe(
            this.recipe.id,
            this.recipe.title,
        ),
        creationDate = this.creationDate,
    )
}