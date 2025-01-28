package main.com.xavierclavel.controllertests

import com.xavierclavel.ApplicationTest
import common.dto.IngredientDTO
import common.dto.RecipeDTO
import common.dto.RecipeDTO.RecipeIngredientDTO
import common.enums.CookbookRole
import common.enums.DishClass
import common.infodto.RecipeInfo
import common.infodto.UserInfo
import org.junit.Test
import kotlin.test.assertEquals
import main.com.xavierclavel.utils.addCookbookRecipe
import main.com.xavierclavel.utils.addCookbookUser
import main.com.xavierclavel.utils.createCookbook
import main.com.xavierclavel.utils.createIngredient
import main.com.xavierclavel.utils.createLike
import main.com.xavierclavel.utils.createRecipe
import main.com.xavierclavel.utils.createUser
import main.com.xavierclavel.utils.getMe
import main.com.xavierclavel.utils.getRecipe
import main.com.xavierclavel.utils.listRecipes

class RecipeFiltersTest : ApplicationTest() {

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
        var adminUser: UserInfo? = null
        val recipeDTO1 = RecipeDTO(title = "", dishClass = DishClass.ENTREE)
        val recipeDTO2 = RecipeDTO(title = "", dishClass = DishClass.MAIN_DISH)
        val recipeDTO3 = RecipeDTO(title = "", dishClass = DishClass.DESERT)
        runAsAdmin {
            adminUser = client.getMe()

            val recipe1 = client.createRecipe(recipeDTO1)
            val recipe2 = client.createRecipe(recipeDTO2)
            val recipe3 = client.createRecipe(recipeDTO3)

            val response1 = client.listRecipes(dishClasses = setOf(DishClass.ENTREE))
            assertEquals(setOf(recipe1), response1.toSet())

            val response2 = client.listRecipes(dishClasses = setOf(DishClass.MAIN_DISH))
            assertEquals(setOf(recipe2), response2.toSet())

            val response3 = client.listRecipes(dishClasses = setOf(DishClass.DESERT))
            assertEquals(setOf(recipe3), response3.toSet())

            val response4 = client.listRecipes(dishClasses = setOf(DishClass.ENTREE, DishClass.MAIN_DISH))
            assertEquals(setOf(recipe1, recipe2), response4.toSet())

            val response5 = client.listRecipes(dishClasses = setOf(DishClass.ENTREE, DishClass.MAIN_DISH, DishClass.DESERT))
            assertEquals(setOf(recipe1, recipe2, recipe3), response5.toSet())


        }
    }

    @Test
    fun `filter recipes by likes and dish class`() = runTest {
        var adminUser: UserInfo? = null
        val recipeDTO1 = RecipeDTO(title = "", dishClass = DishClass.MAIN_DISH)
        val recipeDTO2 = RecipeDTO(title = "", dishClass = DishClass.MAIN_DISH)
        val recipeDTO3 = RecipeDTO(title = "", dishClass = DishClass.DESERT)
        runAsAdmin {
            adminUser = client.getMe()

            val recipe1 = client.createRecipe(recipeDTO1)
            val recipe2 = client.createRecipe(recipeDTO2)
            val recipe3 = client.createRecipe(recipeDTO3)

            client.createLike(recipe1.id)

            val recipe1Updated = client.getRecipe(recipe1.id)


            val response1 = client.listRecipes(dishClasses = setOf(DishClass.MAIN_DISH))
            assertEquals(setOf(recipe1Updated, recipe2), response1.toSet())

            val response2 = client.listRecipes(likedBy = adminUser.id, dishClasses = setOf(DishClass.MAIN_DISH))
            assertEquals(setOf(recipe1Updated), response2.toSet())

        }
    }

    @Test
    fun `filter recipes by ingredient`() = runTestAsAdmin {
        val ingredientDTO1 = IngredientDTO("")
        val ingredientDTO2 = IngredientDTO("")

        val ingredient1 = client.createIngredient(ingredientDTO1)
        val ingredient2 = client.createIngredient(ingredientDTO2)

        val recipeIngredient1 = RecipeIngredientDTO(ingredient1.id)
        val recipeIngredient2 = RecipeIngredientDTO(ingredient2.id)

        val recipeDTO1 = RecipeDTO(title = "", ingredients = mutableListOf(recipeIngredient1))
        val recipeDTO2 = RecipeDTO(title = "", ingredients = mutableListOf(recipeIngredient2))
        val recipeDTO3 = RecipeDTO(title = "", ingredients = mutableListOf(recipeIngredient1, recipeIngredient2))

        val recipe1 = client.createRecipe(recipeDTO1)
        val recipe2 = client.createRecipe(recipeDTO2)
        val recipe3 = client.createRecipe(recipeDTO3)

        val response1 = client.listRecipes(ingredient = setOf(ingredient1.id))
        assertEquals(setOf(recipe1, recipe3), response1.toSet())

        val response2 = client.listRecipes(ingredient = setOf(ingredient2.id))
        assertEquals(setOf(recipe2, recipe3), response2.toSet())

        val response3 = client.listRecipes(ingredient = setOf(ingredient1.id, ingredient2.id))
        assertEquals(setOf(recipe3), response3.toSet())
    }

    @Test
    fun `filter recipes by title`() = runTestAsAdmin {
        val recipeDTO1 = RecipeDTO(title = "Tarte aux champignons")
        val recipeDTO2 = RecipeDTO(title = "Tarte aux pommes")

        val recipe1 = client.createRecipe(recipeDTO1)
        val recipe2 = client.createRecipe(recipeDTO2)

        val response1 = client.listRecipes(search = "champignons")
        assertEquals(setOf(recipe1), response1.toSet())

        val response2 = client.listRecipes(search = "tarte")
        assertEquals(setOf(recipe1,recipe2), response2.toSet())
    }
}