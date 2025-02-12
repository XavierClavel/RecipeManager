package com.xavierclavel.controllers

import com.xavierclavel.controllers.AuthController.getSessionUserId
import com.xavierclavel.exceptions.ForbiddenCause
import com.xavierclavel.exceptions.ForbiddenException
import com.xavierclavel.services.CustomIngredientService
import com.xavierclavel.services.ImageService
import com.xavierclavel.services.RecipeIngredientService
import com.xavierclavel.services.RecipeService
import com.xavierclavel.services.UserService
import com.xavierclavel.utils.Controller
import com.xavierclavel.utils.checkRecipeEditionRights
import com.xavierclavel.utils.getIdPathVariable
import com.xavierclavel.utils.getIdPathVariableSet
import com.xavierclavel.utils.getPaging
import com.xavierclavel.utils.getPathId
import com.xavierclavel.utils.getSort
import com.xavierclavel.utils.logger
import common.RecipeFilter
import common.dto.RecipeDTO
import common.enums.DishClass
import common.infodto.RecipeInfo
import common.utils.URL.RECIPE_URL
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.RoutingContext
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import org.koin.java.KoinJavaComponent.inject

object RecipeController: Controller(RECIPE_URL) {
    val recipeService: RecipeService by inject(RecipeService::class.java)
    val recipeIngredientService: RecipeIngredientService by inject(RecipeIngredientService::class.java)
    val customIngredientService: CustomIngredientService by inject(CustomIngredientService::class.java)
    val userService: UserService by inject(UserService::class.java)
    val imageService: ImageService by inject(ImageService::class.java)

    override fun Route.routes() {
        getRecipe()
        listRecipes()
        createRecipe()
        updateRecipe()
        deleteRecipe()
    }

    private fun Route.getRecipe() = get("/{id}") {
        val recipeId = getPathId()
        val recipe = recipeService.getById(recipeId)
        call.respond(recipe)
    }

    private fun Route.listRecipes() = get {
        val paging = getPaging()
        val sort = getSort()
        val recipeFilter = RecipeFilter(
            user = getIdPathVariable(RecipeFilter::user.name),
            likedBy = getIdPathVariable(RecipeFilter::likedBy.name),
            cookbook = getIdPathVariable(RecipeFilter::cookbook.name),
            cookbookUser = getIdPathVariable(RecipeFilter::cookbookUser.name),
            followedBy = getIdPathVariable(RecipeFilter::followedBy.name),
            ingredient = getIdPathVariableSet(RecipeFilter::ingredient.name),
            dishClasses = call.parameters[RecipeFilter::dishClasses.name]?.split(",")?.map { DishClass.valueOf(it.trim()) }?.toSet() ?: setOf(),
            search = call.parameters[RecipeFilter::search.name],
        )
        logger.info {"paging : $paging"}
        logger.info {"sort : $sort"}
        logger.info {"filter : $recipeFilter"}
        val result = if (recipeFilter.hasAdditiveFilters()) {
            recipeService.findList(
                paging = paging,
                sort = sort,
                recipeFilter = recipeFilter,
            )
        } else listOf()

        call.respond(result)
    }

    private fun Route.createRecipe() = post {
        try {
            val user = userService.getEntityById(getSessionUserId())
            val recipeDto = call.receive<RecipeDTO>()
            val recipe = recipeService.createRecipe(recipeDto, user)
            recipeIngredientService.updateRecipeIngredients(recipe.id, recipeDto)
            customIngredientService.updateCustomIngredients(recipe.id, recipeDto)
            val recipeInfo = recipeService.getById(recipe.id)
            call.respond(HttpStatusCode.Created, recipeInfo)
        } catch (e: Exception) {
            logger.error {e.message}
        }

    }

    private fun Route.updateRecipe() = put("/{id}") {
        try {
            val recipeId = getPathId()
            val recipe = recipeService.getById(recipeId)
            checkRecipeEditionRights(recipe.owner.id)
            val recipeDto = call.receive<RecipeDTO>()
            recipeIngredientService.updateRecipeIngredients(recipe.id, recipeDto)
            customIngredientService.updateCustomIngredients(recipe.id, recipeDto)
            val info = recipeService.updateRecipe(recipeId, recipeDto)
            call.respond(HttpStatusCode.OK, info)
        } catch (e: Exception) {
            logger.error {e}
        }
    }

    private fun Route.deleteRecipe() = delete("/{id}") {
        val recipeId = getPathId()
        val recipe = recipeService.getById(recipeId)
        checkRecipeEditionRights(recipe.owner.id)
        recipeService.tagRecipeForDeletion(recipeId)
        recipeService.tryDelete(recipeId)
        call.respond(HttpStatusCode.OK)
    }





}