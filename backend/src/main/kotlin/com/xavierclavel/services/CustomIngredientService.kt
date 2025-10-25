package com.xavierclavel.services

import com.xavierclavel.models.jointables.CustomIngredient
import com.xavierclavel.models.jointables.query.QCustomIngredient
import com.xavierclavel.utils.DbTransaction.insertAndGet
import shared.dto.RecipeDTO
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CustomIngredientService: KoinComponent {
    val ingredientService: IngredientService by inject()
    val recipeService: RecipeService by inject()

    fun countAll() =
            QCustomIngredient().findCount()

    fun findEntitiesByRecipe(recipeId: Long) =
        QCustomIngredient().where().recipe.id.eq(recipeId).findList()

    fun updateCustomIngredients(recipeId: Long, recipeDTO: RecipeDTO) {
        val customIngredients = findEntitiesByRecipe(recipeId)

        // Remove all current recipe ingredients no longer present in input
        customIngredients
            .filter {current -> recipeDTO.customIngredients.none { it.name == current.name } }
            .onEach { it.delete() }

        recipeDTO.customIngredients.forEach { recipeIngredientDTO ->
            customIngredients
                // Perform update
                .find { it.name == recipeIngredientDTO.name }
                ?.mergeDTO(recipeIngredientDTO)
                ?.update()

                // Perform creation
                ?: CustomIngredient(
                    recipe = recipeService.findEntityById(recipeId),
                    name = recipeIngredientDTO.name,
                ).mergeDTO(recipeIngredientDTO)
                .insertAndGet()
        }
    }
}