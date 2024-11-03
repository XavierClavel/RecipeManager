package com.xavierclavel.services

import com.xavierclavel.models.Recipe
import com.xavierclavel.models.User
import com.xavierclavel.models.query.QRecipe
import com.xavierclavel.utils.DbTransaction.insertAndGet
import com.xavierclavel.utils.DbTransaction.updateAndGet
import common.dto.RecipeDTO
import common.infodto.RecipeInfo
import org.koin.core.component.KoinComponent

class RecipeService: KoinComponent {
    fun countAll() =
        QRecipe().findCount()

    fun findList() : List<RecipeInfo> =
        QRecipe().findList().map { it.toInfo() }

    fun findById(recipeId: Long) : RecipeInfo? =
        QRecipe().id.eq(recipeId).findOne()?.toInfo()

    fun findAllByCircleId(circleId: Long): List<Recipe> =
        QRecipe().circles.id.eq(circleId).findList()

    fun findAllByOwnerId(userId: Long): List<Recipe> =
        QRecipe().owner.id.eq(userId).findList()

    fun findAllByOwnerUsername(username: String): List<Recipe> =
        QRecipe().owner.username.eq(username).findList()

    fun deleteById(recipeId: Long) {
        QRecipe().id.eq(recipeId).delete()
    }

    fun getRecipeOwner(recipeId: Long) = findById(recipeId)!!.owner

    fun createRecipe(recipeDTO: RecipeDTO, owner: User): RecipeInfo =
        Recipe()
            .mergeDTO(recipeDTO)
            .setOwner(owner)
            .insertAndGet()
            .toInfo()


    fun updateRecipe(id: Long, recipeDTO: RecipeDTO): RecipeInfo? =
        QRecipe().id.eq(id).findOne()?.mergeDTO(recipeDTO)?.updateAndGet()?.toInfo()


    fun deleteRecipe(id: Long) {
        QRecipe().id.eq(id).delete()
    }

}