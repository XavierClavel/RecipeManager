package com.xavierclavel.models

import com.xavierclavel.models.jointables.Like
import com.xavierclavel.models.jointables.RecipeIngredient
import com.xavierclavel.models.jointables.CustomIngredient
import common.dto.RecipeDTO
import common.infodto.RecipeInfo
import jakarta.persistence.CascadeType
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import io.ebean.Model
import java.time.Instant

@Entity
@Table(name = "recipes")
class Recipe (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    var localId : Long = -1,

    var title: String = "",

    var description: String = "",

    var creationDate: Long = Instant.now().epochSecond,

    var modificationDate: Long = Instant.now().epochSecond,

    var tips: List<String> = listOf(),

    //metadata
    var yield: Int = 0,
    var preparationTime: Int = 0,
    var cookingTime: Int = 0,
    var cookingTemperature: Int = 0,
    var conservationTime: Int = 0,


    @ElementCollection
    var steps: List<String> = listOf(),

    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    var ingredients: List<RecipeIngredient> = listOf(),

    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    var customIngredients: List<CustomIngredient> = listOf(),


    @ManyToOne
    var owner: User? = null,

    @ManyToMany
    var cookbooks: MutableSet<Cookbook> = mutableSetOf(),

    @OneToMany(mappedBy = "recipe", cascade = [CascadeType.ALL], orphanRemoval = true)
    var likes: MutableSet<Like> = mutableSetOf(),

    ) : Model() {
    fun mergeDTO(recipeDTO: RecipeDTO) : Recipe = apply {
        this.title = recipeDTO.title
        this.description = recipeDTO.description
        this.steps = recipeDTO.steps
        this.modificationDate = Instant.now().epochSecond
    }

    fun setOwner(user: User): Recipe = apply {
        this.owner = user
    }

    fun toInfo() = RecipeInfo(
        id = this.id,
        title = title,
        description = description,
        steps = steps,
        ingredients = listOf(),
        owner = this.owner!!.username,
        creationDate = this.creationDate,
        editionDate = this.modificationDate,
    )
}