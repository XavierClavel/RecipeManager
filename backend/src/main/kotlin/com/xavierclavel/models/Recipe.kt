package com.xavierclavel.models

import com.xavierclavel.models.jointables.CookbookRecipe
import com.xavierclavel.models.jointables.Like
import com.xavierclavel.models.jointables.RecipeIngredient
import com.xavierclavel.models.jointables.CustomIngredient
import common.dto.RecipeDTO
import common.enums.DishClass
import common.enums.Locale
import common.infodto.RecipeInfo
import jakarta.persistence.CascadeType
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import io.ebean.Model
import io.ebean.annotation.NotNull
import java.time.Instant

@Entity
@Table(name = "recipes")
class Recipe (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    var title: String = "",

    var description: String = "",

    var dishClass: DishClass = DishClass.MAIN_DISH,

    var creationDate: Long = Instant.now().epochSecond,

    var modificationDate: Long = Instant.now().epochSecond,

    var tips: List<String> = listOf(),

    //metadata
    var yield: Int? = null,
    var preparationTime: Int? = null,
    var cookingTime: Int? = null,
    var cookingTemperature: Int? = null,


    @ElementCollection
    var steps: List<String> = listOf(),

    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL], orphanRemoval = true)
    var ingredients: List<RecipeIngredient> = listOf(),

    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL], orphanRemoval = true)
    var customIngredients: List<CustomIngredient> = listOf(),


    @ManyToOne
    var owner: User? = null,

    @OneToMany(mappedBy = "recipe", cascade = [CascadeType.ALL], orphanRemoval = true)
    var cookbooks: MutableList<CookbookRecipe> = mutableListOf(),

    @OneToMany(mappedBy = "recipe", cascade = [CascadeType.ALL], orphanRemoval = true)
    var likes: MutableList<Like> = mutableListOf(),

    var taggedForDeletion: Boolean = false,

    ) : Model() {
    fun mergeDTO(recipeDTO: RecipeDTO) : Recipe = apply {
        this.title = recipeDTO.title
        this.description = recipeDTO.description
        this.dishClass = recipeDTO.dishClass

        this.steps = recipeDTO.steps
        this.modificationDate = Instant.now().epochSecond

        this.yield = recipeDTO.yield
        this.preparationTime = recipeDTO.preparationTime
        this.cookingTime = recipeDTO.cookingTime
        this.cookingTemperature = recipeDTO.cookingTemperature
    }

    fun setOwner(user: User): Recipe = apply {
        this.owner = user
    }

    fun toInfo(locale: Locale) = RecipeInfo(
        id = this.id,
        title = title,
        description = description,
        dishClass = dishClass,
        steps = steps,
        ingredients = ingredients.map { it.toInfo(locale) },
        customIngredients = customIngredients.map {it.toInfo()},

        owner = this.owner!!.toOverview(),
        creationDate = this.creationDate,
        editionDate = this.modificationDate,
        likesCount = this.likes.size,

        yield = this.yield,
        preparationTime = this.preparationTime,
        cookingTime = this.cookingTime,
        cookingTemperature = this.cookingTemperature,
    )

    fun tagForDeletion(): Recipe = this.apply {
            taggedForDeletion = true
    }

    fun hasReferences(): Boolean =
        likes.isNotEmpty() && cookbooks.isNotEmpty()
}