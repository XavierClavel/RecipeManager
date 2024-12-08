package com.xavierclavel.services

import com.xavierclavel.models.Recipe
import com.xavierclavel.models.User
import com.xavierclavel.models.jointables.query.QLike
import com.xavierclavel.models.query.QRecipe
import com.xavierclavel.utils.DbTransaction.insertAndGet
import com.xavierclavel.utils.DbTransaction.updateAndGet
import com.xavierclavel.utils.logger
import common.dto.RecipeDTO
import common.enums.Sort
import common.infodto.RecipeInfo
import io.ebean.FetchConfig
import io.ebean.Paging
import org.koin.core.component.KoinComponent

class RecipeService: KoinComponent {
    val likeAlias = QLike.alias()

    fun countAll() =
        QRecipe().findCount()

    fun countByOwner(username: String) =
        queryByOwner(username).findCount()


    fun findList(paging: Paging, sort: Sort, owner: Long?, likedBy: Long?, cookbook: Long?) : List<RecipeInfo> =
        QRecipe()
            .fetch(QRecipe.Alias.likes.toString(), "count(*)", FetchConfig.ofLazy()) // Aggregate likes
            .having().raw("count(likes.id) >= 0") // Include recipes with no likes
            .filterByOwner(owner)
            .filterByLikes(likedBy)
            .filterByCookbook(cookbook)
            .setPaging(paging)
            .sort(sort)
            .findList()
            .map {it.toInfo()}
        /*
        QRecipe()
            .fetch("likes")  // Forces LEFT JOIN
            .select(QRecipe.Alias.id, QRecipe.alias().likesCount)
            .select(QRecipe.Alias.id)
            .filterByOwner(owner)
            .filterByLikes(likedBy)
            .filterByCookbook(cookbook)
            .sort(sort)
            .setPaging(paging)
            .findList()
            .map { it.toInfo() }

         */

    fun findEntityById(recipeId: Long) : Recipe? =
        QRecipe().id.eq(recipeId).findOne()

    fun findById(recipeId: Long) : RecipeInfo? =
        findEntityById(recipeId)?.toInfo()


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


    private fun QRecipe.filterByOwner(userId: Long?) =
        if (userId == null) this else this.owner.id.eq(userId)

    private fun QRecipe.filterByLikes(userId: Long?) =
        if (userId == null) this else this.likes.user.id.eq(userId)

    private fun QRecipe.filterByCookbook(cookbookId: Long?) =
        if (cookbookId == null) this else this.cookbooks.id.eq(cookbookId)

    private fun QRecipe.sort(sort: Sort) =
        when (sort) {
            Sort.NONE -> this
            Sort.NAME_ASCENDING -> this.orderBy().title.asc()
            Sort.NAME_DESCENDING -> this.orderBy().title.desc()
            Sort.LIKES_ASCENDING -> this.orderBy("count(likes.id) asc")
            Sort.LIKES_DESCENDING -> this.orderBy("count(likes.id) desc")
            Sort.DATE_ASCENDING -> this.orderBy().creationDate.asc()
            Sort.DATE_DESCENDING -> this.orderBy().creationDate.desc()
        }

}