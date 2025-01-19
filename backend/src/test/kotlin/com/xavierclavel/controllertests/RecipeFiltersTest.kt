package main.com.xavierclavel.controllertests

import com.xavierclavel.ApplicationTest
import com.xavierclavel.utils.logger
import common.dto.RecipeDTO
import common.dto.RecipeDTO.RecipeIngredientDTO
import common.enums.AmountUnit
import common.enums.CookbookRole
import common.enums.Sort
import common.infodto.RecipeInfo
import common.infodto.RecipeIngredientInfo
import common.infodto.UserInfo
import common.utils.URL.RECIPE_URL
import io.ktor.client.request.header
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import org.junit.Test
import kotlin.test.assertEquals
import io.ktor.client.request.put
import junit.framework.TestCase.assertTrue
import main.com.xavierclavel.utils.addCookbookRecipe
import main.com.xavierclavel.utils.addCookbookUser
import main.com.xavierclavel.utils.assertRecipeExists
import main.com.xavierclavel.utils.createCookbook
import main.com.xavierclavel.utils.createIngredient
import main.com.xavierclavel.utils.createLike
import main.com.xavierclavel.utils.createRecipe
import main.com.xavierclavel.utils.createUser
import main.com.xavierclavel.utils.deleteRecipe
import main.com.xavierclavel.utils.getMe
import main.com.xavierclavel.utils.getRecipe
import main.com.xavierclavel.utils.listRecipes
import main.com.xavierclavel.utils.updateRecipe
import kotlin.test.assertFalse

class RecipeFiltersTest : ApplicationTest() {

    val recipeDTO = RecipeDTO(
        title = "My recipe",
        description = "My description",
        steps = mutableListOf(
            "cut",
            "cook"
        )
    )

    @Test
    fun `filter recipes by liked`() = runTest {
        var user: UserInfo? = null
        var adminUser: UserInfo? = null
        var recipe: RecipeInfo? = null
        runAsAdmin {
            user = client.createUser()
            adminUser = client.getMe()
        }
        runAs(user!!.username) {
            recipe = client.createRecipe()
        }
        runAsAdmin {
            val response1 = client.listRecipes(owner = user.id)
            assertEquals(1, response1.size)

            val response2 = client.listRecipes(likedBy = adminUser!!.id)
            assertEquals(0, response2.size)

            client.createLike(recipe!!.id)
            val response3 = client.listRecipes(likedBy = adminUser.id)
            assertEquals(1, response3.size)
        }
    }

    @Test
    fun `filter recipes by  self liked`() = runTest {
        var adminUser: UserInfo? = null
        var recipe: RecipeInfo? = null
        runAsAdmin {
            adminUser = client.getMe()
            recipe = client.createRecipe()

            val response1 = client.listRecipes(owner = adminUser.id)
            assertEquals(1, response1.size)

            val response2 = client.listRecipes(likedBy = adminUser.id)
            assertEquals(0, response2.size)

            client.createLike(recipe.id)
            val response3 = client.listRecipes(likedBy = adminUser.id)
            assertEquals(1, response3.size)
        }
    }

    @Test
    fun `filter recipes by liked and owned`() = runTest {
        var user: UserInfo? = null
        var adminUser: UserInfo? = null
        var recipeOwned: RecipeInfo? = null
        var recipeLiked: RecipeInfo? = null
        runAsAdmin {
            user = client.createUser()
            adminUser = client.getMe()
            recipeOwned = client.createRecipe()
        }
        runAs(user!!.username) {
            recipeLiked = client.createRecipe()
        }
        runAsAdmin {
            client.createLike(recipeLiked!!.id)

            val response1 = client.listRecipes(owner = adminUser!!.id)
            assertEquals(1, response1.size)

            val response2 = client.listRecipes(likedBy = adminUser.id)
            assertEquals(1, response2.size)

            val response3 = client.listRecipes(owner = adminUser!!.id, likedBy = adminUser.id)
            assertEquals(2, response3.size)
        }
    }

    @Test
    fun `filter recipes by user cookbooks`() = runTest {
        var user: UserInfo? = null
        var adminUser: UserInfo? = null
        var recipe1: RecipeInfo? = null
        var recipe2: RecipeInfo? = null
        var recipe3: RecipeInfo? = null
        runAsAdmin {
            user = client.createUser()
            adminUser = client.getMe()
            recipe1 = client.createRecipe()
            recipe2 = client.createRecipe()
            recipe3 = client.createRecipe()

            val cookbook = client.createCookbook()
            client.addCookbookUser(cookbook.id, user.id, CookbookRole.READER)
            client.addCookbookRecipe(cookbook.id, recipe1.id)
            client.addCookbookRecipe(cookbook.id, recipe2.id)
        }

        runAs(user!!.username) {
            val response1 = client.listRecipes(cookbookUser = user.id)
            assertEquals(2, response1.size)
            assertEquals(setOf(recipe1, recipe2), response1.toSet())
        }

    }

    @Test
    fun `filter recipes by dish class`() = runTest {
        var user: UserInfo? = null
        var adminUser: UserInfo? = null
        var recipeOwned: RecipeInfo? = null
        var recipeLiked: RecipeInfo? = null
        runAsAdmin {
            user = client.createUser()
            adminUser = client.getMe()
            recipeOwned = client.createRecipe()
        }
        runAs(user!!.username) {
            recipeLiked = client.createRecipe()
        }
        runAsAdmin {
            client.createLike(recipeLiked!!.id)

            val response1 = client.listRecipes(owner = adminUser!!.id)
            assertEquals(1, response1.size)

            val response2 = client.listRecipes(likedBy = adminUser.id)
            assertEquals(1, response2.size)

            val response3 = client.listRecipes(owner = adminUser!!.id, likedBy = adminUser.id)
            assertEquals(2, response3.size)
        }
    }
}