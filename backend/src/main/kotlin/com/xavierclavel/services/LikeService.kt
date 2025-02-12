package com.xavierclavel.services

import com.xavierclavel.models.jointables.Like
import com.xavierclavel.models.jointables.query.QLike
import com.xavierclavel.utils.DbTransaction.insertAndGet
import common.infodto.LikeInfo
import io.ebean.Paging
import org.koin.core.component.KoinComponent
import org.koin.java.KoinJavaComponent.inject

class LikeService: KoinComponent {
    val recipeService by inject<RecipeService>(RecipeService::class.java)
    val userService by inject<UserService>(UserService::class.java)

    fun countAll(): Int =
        QLike().findCount()

    fun countByUser(userId: Long) : Int =
        QLike().filterByUser(userId).findCount()

    fun countByRecipe(recipeId: Long) : Int =
        QLike().filterByRecipe(recipeId).findCount()

    fun find(recipeId: Long?, userId: Long?, paging: Paging): List<LikeInfo> =
        QLike()
            .filterByRecipe(recipeId)
            .filterByUser(userId)
            .setPaging(paging)
            .findList()
            .map { it.toInfo() }

    fun count(recipeId: Long?, userId: Long?): Int =
        QLike()
            .filterByRecipe(recipeId)
            .filterByUser(userId)
            .findCount()

    fun exists(recipeId: Long?, userId: Long?): Boolean =
        QLike()
            .filterByRecipe(recipeId)
            .filterByUser(userId)
            .exists()

    fun createLike(recipeId: Long, userId: Long): LikeInfo =
        Like(
            recipe = recipeService.findEntityById(recipeId)!!,
            user = userService.getEntityById(userId)!!,
        ).insertAndGet().toInfo()

    fun deleteLike(recipeId: Long, userId: Long) =
        QLike()
            .filterByRecipe(recipeId)
            .filterByUser(userId)
            .findOne()
            ?.delete()



    private fun QLike.filterByUser(id: Long?) =
        if (id == null) this else this.user.id.eq(id)

    private fun QLike.filterByRecipe(id: Long?) =
        if (id == null) this else this.recipe.id.eq(id)

}