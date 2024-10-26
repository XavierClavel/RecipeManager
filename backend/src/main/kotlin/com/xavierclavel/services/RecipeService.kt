package com.xavierclavel.services

import com.xavierclavel.models.Recipe
import com.xavierclavel.models.query.QRecipe
import org.koin.core.component.KoinComponent

class RecipeService: KoinComponent {
    fun countAll() =
        QRecipe().findCount()

    fun findById(recipeId: Long) : Recipe? =
        QRecipe().id.eq(recipeId).findOne()

    fun findAllByCircleId(circleId: Long): List<Recipe> =
        QRecipe().circles.id.eq(circleId).findList()

    fun findAllByOwnerId(userId: Long): List<Recipe> =
        QRecipe().owner.id.eq(userId).findList()

}