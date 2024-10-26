package com.xavierclavel.services

import com.xavierclavel.models.Recipe
import com.xavierclavel.models.jointables.Like
import com.xavierclavel.models.jointables.query.QLike
import com.xavierclavel.models.query.QRecipe
import org.koin.core.component.KoinComponent

class LikeService: KoinComponent {
    fun countAll() =
        QLike().findCount()

    fun findAllByRecipeId(recipeId: Long): List<Like> =
        QLike().recipe.id.eq(recipeId).findList()

    fun countByRecipeId(recipeId: Long) =
        QLike().recipe.id.eq(recipeId).findCount()


}