package com.xavierclavel.services

import com.xavierclavel.models.Recipe
import com.xavierclavel.models.User
import com.xavierclavel.models.query.QRecipe
import com.xavierclavel.utils.DbTransaction.insertAndGet
import com.xavierclavel.utils.DbTransaction.updateAndGet
import common.dto.RecipeDTO
import common.infodto.RecipeInfo
import io.ebean.Paging
import org.koin.core.component.KoinComponent

class RecipeService: KoinComponent {
    fun countAll() =
        QRecipe().findCount()

    fun countByOwner(username: String) =
        queryByOwner(username).findCount()


    fun findList(paging: Paging, owner: Long?) : List<RecipeInfo> =
        QRecipe()
            .filterByOwner(owner)
            .setPaging(paging)
            .findList()
            .map { it.toInfo() }

    fun findById(recipeId: Long) : RecipeInfo? =
        QRecipe().id.eq(recipeId).findOne()?.toInfo()

    fun findAllByCircleId(circleId: Long): List<Recipe> =
        QRecipe().circles.id.eq(circleId).findList()

    fun findAllByOwnerId(userId: Long): List<Recipe> =
        QRecipe().owner.id.eq(userId).findList()

    fun findAllByOwnerUsername(username: String): List<Recipe> =
        queryByOwner(username).findList()

    fun findByOwner(username: String, paging: Paging): List<RecipeInfo> =
        queryByOwner(username)
            .setPaging(paging)
            .findList()
            .map{it.toInfo()}

    fun deleteById(recipeId: Long) {
        QRecipe().id.eq(recipeId).delete()
    }

    fun getRecipeOwner(recipeId: Long) = findById(recipeId)?.owner

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

    private fun queryByOwner(username: String) =
        QRecipe().owner.username.eq(username)

    private fun QRecipe.filterByOwner(username: String?) =
        if (username == null) this else this.owner.username.eq(username)

    private fun QRecipe.filterByOwner(userId: Long?) =
        if (userId == null) this else this.owner.id.eq(userId)

}