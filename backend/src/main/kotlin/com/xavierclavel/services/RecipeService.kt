package com.xavierclavel.services

import com.xavierclavel.controllers.RecipeController.imageService
import com.xavierclavel.exceptions.NotFoundCause
import com.xavierclavel.exceptions.NotFoundException
import com.xavierclavel.models.Recipe
import com.xavierclavel.models.User
import com.xavierclavel.models.jointables.query.QCookbookRecipe
import com.xavierclavel.models.query.QRecipe
import com.xavierclavel.utils.DbTransaction.insertAndGet
import com.xavierclavel.utils.DbTransaction.updateAndGet
import com.xavierclavel.utils.logger
import common.RecipeFilter
import common.dto.RecipeDTO
import common.enums.DishClass
import common.enums.Sort
import common.infodto.RecipeInfo
import common.utils.Filepath.RECIPES_IMG_PATH
import common.utils.Filepath.RECIPES_THUMBNAIL_PATH
import io.ebean.FetchConfig
import io.ebean.Paging
import org.koin.core.component.KoinComponent

class RecipeService: KoinComponent {

    fun countAll() =
        QRecipe().findCount()

    fun countByOwner(username: String) =
        queryByOwner(username).findCount()


    fun findList(
        requestorId: Long,
        paging: Paging,
        sort: Sort,
        recipeFilter: RecipeFilter,
    ) : List<RecipeInfo> =
        QRecipe()
            .fetch(QRecipe.Alias.likes.toString(), "count(*)", FetchConfig.ofLazy()) // Aggregate likes
            .having().raw("count(likes.id) >= 0") // Ensure recipes with no likes are included
            .filter(recipeFilter)
            .filterOutDeletion(requestorId)
            .setPaging(paging)
            .sort(sort)
            .findList()
            .map { it.toInfo() }

    fun findEntityById(recipeId: Long) : Recipe? =
        QRecipe().id.eq(recipeId).findOne()

    fun getEntityById(recipeId: Long) : Recipe =
        findEntityById(recipeId)
            ?: throw NotFoundException(NotFoundCause.RECIPE_NOT_FOUND)

    fun getById(userId: Long, recipeId: Long) : RecipeInfo =
        QRecipe().id.eq(recipeId)
            .filterOutDeletion(userId)
            .findOne()
            ?.toInfo()
            ?: throw NotFoundException(NotFoundCause.RECIPE_NOT_FOUND)


    fun deleteById(recipeId: Long) {
        QRecipe().id.eq(recipeId).delete()
    }


    fun getRecipeOwner(recipeId: Long) =
        getEntityById(recipeId).owner
            ?: throw NotFoundException(NotFoundCause.RECIPE_NOT_FOUND)

    fun createRecipe(recipeDTO: RecipeDTO, owner: User): RecipeInfo =
        Recipe()
            .mergeDTO(recipeDTO)
            .setOwner(owner)
            .insertAndGet()
            .toInfo()


    fun updateRecipe(id: Long, recipeDTO: RecipeDTO): RecipeInfo =
        getEntityById(id).mergeDTO(recipeDTO).updateAndGet().toInfo()


    fun deleteRecipe(id: Long) {
        QRecipe().id.eq(id).delete()
    }

    fun tagRecipeForDeletion(id: Long) {
        getEntityById(id).tagForDeletion().update()
    }

    fun tryDelete(id: Long) {
        val recipe = getEntityById(id)
        val recipeInfo = recipe.toInfo()
        logger.info {recipe}
        logger.info {"Has references: ${recipeInfo.likesCount > 0 || QCookbookRecipe().recipe.id.eq(recipe.id).exists()}"}
        if (recipeInfo.likesCount > 0 || QCookbookRecipe().recipe.id.eq(recipe.id).exists()) return
        recipe.delete()
        imageService.deleteImage(RECIPES_IMG_PATH, id)
        imageService.deleteImage(RECIPES_THUMBNAIL_PATH, id)
    }

    private fun queryByOwner(username: String) =
        QRecipe().owner.username.eq(username)

    private fun QRecipe.filter(recipeFilter: RecipeFilter) =
        if (!recipeFilter.hasFilters()) this
        else this
            .and()
            .apply {
                if (recipeFilter.hasAdditiveFilters()) {
                    this.or()
                        .filterByLikes(recipeFilter.likedBy)
                        .filterByOwner(recipeFilter.user)
                        .filterByCookbook(recipeFilter.cookbook)
                        .filterByUserCookbooks(recipeFilter.cookbookUser)
                        .filterByFollowed(recipeFilter.followedBy)
                        .endOr()
                }
            }
            .filterByDishClass(recipeFilter.dishClasses)
            .filterByIngredient(recipeFilter.ingredient)
            .filterBySearch(recipeFilter.search)
            .endAnd()


    private fun QRecipe.filterByOwner(userId: Long?) =
        if (userId == null) this
        else this.where()
            .owner.id.eq(userId)

    private fun QRecipe.filterOutDeletion(userId: Long?) =
        if (userId == null) this
        else this.where().not()
            .owner.id.eq(userId).and()
            .taggedForDeletion.eq(true)

    private fun QRecipe.filterByDishClass(dishClasses: Set<DishClass>) =
        if (dishClasses.isEmpty()) this
        else this.where().dishClass.`in`(dishClasses)

    private fun QRecipe.filterByLikes(userId: Long?) =
        if (userId == null) this
        else this//.likes.user.id.eq(userId)
            .where().raw("EXISTS (SELECT 1 FROM likes l WHERE l.recipe_id = ${QRecipe.Alias.id} AND l.user_id = ?)", userId)


    private fun QRecipe.filterByCookbook(cookbookId: Long?) =
        if (cookbookId == null) this
        else this
            .where().raw("""
                EXISTS (
                    SELECT 1 FROM cookbook_recipes cr 
                    WHERE cr.recipe_id = t0.id 
                    AND cr.cookbook_id = ?
                )""".trimIndent(), cookbookId)
            //.where().cookbooks.cookbook.id.eq(cookbookId)

    private fun QRecipe.filterByUserCookbooks(userId: Long?) =
        if (userId == null) this
        else this.raw("""
            EXISTS(
                SELECT 1
                FROM cookbook_recipes cr
                JOIN cookbooks c ON cr.cookbook_id = c.id
                JOIN cookbook_users cu ON cu.cookbook_id = c.id
                WHERE cr.recipe_id = t0.id
                AND cu.user_id = ?
            )""".trimIndent(), userId)

    private fun QRecipe.filterByFollowed(followerId: Long?) =
        if (followerId == null) this
        else this.raw("""
            EXISTS(
                SELECT 1
                FROM follows f
                JOIN users u ON f.user_id = u.id
                WHERE f.follower_id = ?
                AND f.pending = false
                AND t0.owner_id = f.user_id
            )""".trimIndent(), followerId)

    private fun QRecipe.filterByIngredient(ingredientsId: Set<Long>) =
        if (ingredientsId.isEmpty()) this
        else this.raw("""
            EXISTS(
                SELECT 1
                FROM recipe_ingredients ri
                JOIN ingredients i ON ri.ingredient_id = i.id
                WHERE ri.recipe_id = t0.id
                AND i.id = ANY(?)
                GROUP BY ri.recipe_id
                HAVING COUNT(DISTINCT i.id) = ?
        )""".trimIndent(), ingredientsId.toList(), ingredientsId.size)

    private fun QRecipe.filterBySearch(search: String?) =
        if (search.isNullOrEmpty()) this
        else this.title.ilike("%$search%")



    private fun QRecipe.sort(sort: Sort) =
        when (sort) {
            Sort.NONE -> this
            Sort.NAME_ASCENDING -> this.orderBy().title.desc()
            Sort.NAME_DESCENDING -> this.orderBy().title.asc()
            Sort.LIKES_ASCENDING -> this.orderBy("count(likes.id) asc")
            Sort.LIKES_DESCENDING -> this.orderBy("count(likes.id) desc")
            Sort.DATE_ASCENDING -> this.orderBy().creationDate.asc()
            Sort.DATE_DESCENDING -> this.orderBy().creationDate.desc()
            Sort.RANDOM -> this.orderBy("random()")
        }

}