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
import com.xavierclavel.utils.logger
import common.dto.CookbookDTO
import common.dto.UserDTO
import common.enums.CookbookRole
import common.infodto.UserInfo
import common.enums.UserRole
import common.infodto.CookbookInfo
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

    fun deleteCookbook(id: Long): Boolean? =
        findEntityById(id)?.delete()

    fun addUserToCookbook(cookbookId: Long, userId: Long, role: CookbookRole) {
        val cookbook = findEntityById(cookbookId)!!
        val user = userService.findEntityById(userId)
        val cookbookUser = CookbookUser(
            user = user,
            role = role,
        ).insertAndGet()
        /*
        return cookbook
            .apply {
                users.add(cookbookUser)
            }
            .updateAndGet()
            .toInfo()

         */
    }

    fun addRecipeToCookbook(cookbookId: Long, recipeId: Long, userId: Long) =
        CookbookRecipe(
            cookbook = findEntityById(cookbookId) ?: throw NotFoundException("Cookbook $cookbookId not found"),
            addedBy = userService.findEntityById(userId) ?: throw NotFoundException("User $userId not found"),
            recipe = recipeService.findEntityById(recipeId) ?: throw NotFoundException("Recipe $recipeId not found"),
        ).insert()

    fun removeRecipeFromCookbook(cookbookId: Long, recipeId: Long): Boolean? =
        QCookbookRecipe()
            .recipe.id.eq(recipeId)
            .and()
            .cookbook.id.eq(cookbookId)
            .findOne()
            ?.delete()


    fun removeUserFromCookbook(cookbookId: Long, userId: Long): Boolean? =
        QCookbookUser()
            .user.id.eq(userId)
            .and()
            .cookbook.id.eq(cookbookId)
            .findOne()
            ?.delete()


}