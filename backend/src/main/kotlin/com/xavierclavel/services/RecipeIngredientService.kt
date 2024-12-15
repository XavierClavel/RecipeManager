package com.xavierclavel.services

import com.xavierclavel.exceptions.NotFoundException
import com.xavierclavel.models.jointables.RecipeIngredient
import com.xavierclavel.models.jointables.query.QRecipeIngredient
import com.xavierclavel.utils.DbTransaction.insertAndGet
import com.xavierclavel.utils.log
import com.xavierclavel.utils.logger
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
        val recipe = recipeService.findEntityById(recipeId)

        val partition = recipeDTO.ingredients.partition { dto -> recipeIngredients.any {it.ingredient!!.id == dto.id} }
        //Deletions
        recipeIngredients
            .filter {current -> recipeDTO.ingredients.none { it.id == current.ingredient?.id } }
            .onEach { it.delete() }

        //Updates
        partition.first.onEach { dto ->
            recipeIngredients.find {it.ingredient?.id == dto.id}
                ?.takeIf { !it.compareTo(dto) }
                ?.mergeDTO(dto)
                ?.update()
        }

        //Creations
        partition.second.onEach { dto ->
            val ingredient = ingredientService.findEntityById(dto.id) ?: throw NotFoundException("Ingredient ${dto.id} not found")
            RecipeIngredient(
                recipe = recipe,
                ingredient = ingredient,
            ).mergeDTO(dto)
            .insertAndGet()
        }
    }
}