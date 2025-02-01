package com.xavierclavel.models

import com.xavierclavel.models.jointables.CookbookRecipe
import com.xavierclavel.models.jointables.CookbookUser
import common.dto.CookbookDTO
import common.infodto.CookbookInfo
import common.overviewdto.CookbookOverview
import common.overviewdto.CookbookRecipeOverview
import io.ebean.Model
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(name = "cookbooks")
class Cookbook (

    @Id
    @GeneratedValue
    var id: Long = 0,

    var title: String = "",

    var description: String = "",

    var creationDate: Long = Instant.now().epochSecond,

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    var recipes: MutableSet<CookbookRecipe> = mutableSetOf(),

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    var users: MutableSet<CookbookUser> = mutableSetOf(),

    var isPublic: Boolean = false,
): Model() {
    companion object {
        fun from(cookbookDTO: CookbookDTO)= Cookbook(
            title = cookbookDTO.title,
            description = cookbookDTO.description,
        )
    }

    fun merge(cookbookDTO: CookbookDTO) = this.apply {
        this.title = cookbookDTO.title
        this.description = cookbookDTO.description
    }

    fun toInfo() = CookbookInfo(
        id = this.id,
        title = this.title,
        description = this.description,
        recipesCount = this.recipes.size,
        usersCount = this.users.size,
        members = this.users.take(5).map { it.user.toOverview() }
    ,
    )
    fun toOverview() = CookbookOverview(
        id = this.id,
        title = this.title,
    )

    fun toRecipeOverview(present: Boolean) = CookbookRecipeOverview(
        id = this.id,
        title = this.title,
        hasRecipe = present,
    )

}