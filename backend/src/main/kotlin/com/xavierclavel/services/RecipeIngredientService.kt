package com.xavierclavel.services

import com.xavierclavel.models.jointables.RecipeIngredient
import com.xavierclavel.models.jointables.query.QRecipeIngredient
import com.xavierclavel.utils.DbTransaction.insertAndGet
import common.dto.RecipeDTO
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class RecipeIngredientService: KoinComponent {
    val ingredientService: IngredientService by inject()
    val recipeService: RecipeService by inject()

    fun countAll() =
            QRecipeIngredient().findCount()

    fun findEntitiesByRecipe(recipeId: Long) =
        QRecipeIngredient().where().recipe.id.eq(recipeId).findList()

    fun updateRecipeIngredients(recipeId: Long, recipeDTO: RecipeDTO) {
        val recipeIngredients = findEntitiesByRecipe(recipeId)

        // Remove all current recipe ingredients no longer present in input
        recipeIngredients
            .filter {current -> recipeDTO.ingredients.none { it.id == current.id } }
            .onEach { it.delete() }

        recipeDTO.ingredients.forEach { recipeIngredientDTO ->
            recipeIngredients
                // Perform update
                .find { it.ingredient!!.id == recipeIngredientDTO.id }
                ?.mergeDTO(recipeIngredientDTO)
                ?.update()

                // Perform creation
                ?: RecipeIngredient(
                    recipe = recipeService.findEntityById(recipeId),
                    ingredient = ingredientService.findEntityById(recipeIngredientDTO.id),
                ).mergeDTO(recipeIngredientDTO)
                .insertAndGet()
        }
    }
}