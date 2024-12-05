package com.xavierclavel.services

import at.favre.lib.crypto.bcrypt.BCrypt
import com.xavierclavel.exceptions.NotFoundException
import com.xavierclavel.models.Cookbook
import com.xavierclavel.models.User
import com.xavierclavel.models.jointables.CookbookRecipe
import com.xavierclavel.models.jointables.CookbookUser
import com.xavierclavel.models.jointables.query.QCookbookRecipe
import com.xavierclavel.models.jointables.query.QCookbookUser
import com.xavierclavel.models.query.QCookbook
import com.xavierclavel.models.query.QUser
import com.xavierclavel.utils.DbTransaction.insertAndGet
import com.xavierclavel.utils.DbTransaction.updateAndGet
import com.xavierclavel.utils.Extensions.page
import com.xavierclavel.utils.logger
import common.dto.CookbookDTO
import common.dto.UserDTO
import common.enums.CookbookRole
import common.infodto.UserInfo
import common.enums.UserRole
import common.infodto.CookbookInfo
import common.infodto.CookbookRecipeInfo
import common.infodto.CookbookUserInfo
import common.infodto.RecipeInfo
import io.ebean.Paging
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CookbookService: KoinComponent {
    val userService: UserService by inject()
    val recipeService: RecipeService by inject()


    fun countAll() =
        QCookbook().findCount()

    fun findEntityById(id: Long) : Cookbook? =
        QCookbook().id.eq(id).findOne()


    fun createCookbook(cookbookDTO: CookbookDTO): CookbookInfo =
        Cookbook.from(cookbookDTO).insertAndGet().toInfo()

    fun getCookbook(id: Long) = findEntityById(id)?.toInfo()

    fun getCookbookUsers(id: Long, paging: Paging): List<CookbookUserInfo>? =
        QCookbook()
            .id.eq(id)
            .findOne()
            ?.users
            ?.map { it.toInfo() }
            ?.page(paging)

    fun getCookbookRecipes(id: Long, paging: Paging): List<CookbookRecipeInfo>? =
        QCookbook()
            .id.eq(id)
            .findOne()
            ?.recipes
            ?.map { it.toInfo() }
            ?.page(paging)

    fun updateCookbook(id: Long, cookbookDTO: CookbookDTO) =
        findEntityById(id)
            ?.merge(cookbookDTO)
            ?.updateAndGet()
            ?.toInfo()

    fun deleteCookbook(id: Long): Boolean? =
        findEntityById(id)?.delete()

    fun addUserToCookbook(cookbookId: Long, userId: Long, role: CookbookRole) {
        val cookbook = findEntityById(cookbookId) ?: throw NotFoundException("Cookbook not found")
        val user = userService.findEntityById(userId) ?: throw NotFoundException("User not found")
        val cookbookUser = CookbookUser(
            user = user,
            role = role,
            cookbook = cookbook,
        ).insertAndGet()
    }

    fun addRecipeToCookbook(cookbookId: Long, recipeId: Long, userId: Long) {
        val cookbook = findEntityById(cookbookId) ?: throw NotFoundException("Cookbook $cookbookId not found")
        val recipe = recipeService.findEntityById(recipeId) ?: throw NotFoundException("Recipe $recipeId not found")
        val cookbookRecipe = CookbookRecipe(
            cookbook = cookbook,
            addedBy = userService.findEntityById(userId) ?: throw NotFoundException("User $userId not found"),
            recipe = recipe,
        ).insertAndGet()

    }


    fun removeRecipeFromCookbook(cookbookId: Long, recipeId: Long): Boolean? =
        QCookbookRecipe()
            .recipe.id.eq(recipeId)
            .and()
            .cookbook.id.eq(cookbookId)
            .findOne()
            ?.delete()


    fun removeUserFromCookbook(cookbookId: Long, userId: Long): Boolean? =
        QCookbookUser()
            .apply { logger.info {cookbookId} }
            .apply { logger.info { userId } }
            .user.id.eq(userId)
            .and()
            .cookbook.id.eq(cookbookId)
            .findOne()
            ?.delete()


}