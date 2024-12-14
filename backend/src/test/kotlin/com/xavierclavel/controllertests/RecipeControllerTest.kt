package main.com.xavierclavel.controllertests

import com.xavierclavel.ApplicationTest
import com.xavierclavel.models.query.QRecipe
import com.xavierclavel.utils.logger
import common.dto.RecipeDTO
import common.dto.RecipeDTO.RecipeIngredientDTO
import common.enums.AmountUnit
import common.enums.Sort
import common.infodto.RecipeIngredientInfo
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
import main.com.xavierclavel.utils.assertRecipeExists
import main.com.xavierclavel.utils.createIngredient
import main.com.xavierclavel.utils.createLike
import main.com.xavierclavel.utils.createRecipe
import main.com.xavierclavel.utils.createUser
import main.com.xavierclavel.utils.deleteRecipe
import main.com.xavierclavel.utils.getRecipe
import main.com.xavierclavel.utils.listRecipes
import main.com.xavierclavel.utils.updateRecipe
import kotlin.math.exp
import kotlin.test.assertFalse

class RecipeControllerTest : ApplicationTest() {

    val recipeDTO = RecipeDTO(
        title = "My recipe",
        description = "My description",
        steps = mutableListOf(
            "cut",
            "cook"
        )
    )

    @Test
    fun `create recipe`() = runTestAsAdmin {
        val response = it.createRecipe(recipeDTO)
        it.assertRecipeExists(response.id)
    }

    @Test
    fun `create recipe with ingredients`() = runTestAsAdmin {
        val ingredient1 = it.createIngredient()
        val ingredient2 = it.createIngredient()

        val recipeIngredient1 = RecipeDTO.RecipeIngredientDTO(
            id = ingredient1.id,
            unit = AmountUnit.GRAM,
            amount = 1f,
        )

        val recipeIngredient2 = RecipeDTO.RecipeIngredientDTO(
            id = ingredient2.id,
            unit = AmountUnit.GRAM,
            amount = 1f,
        )
        val recipeDto = RecipeDTO(
            title = "My recipe",
            ingredients = mutableListOf(recipeIngredient1, recipeIngredient2)
        )
        val recipe = it.createRecipe(recipeDto)
        val response = it.getRecipe(recipe.id)
        it.assertRecipeExists(response.id)
        assertEquals(2, response.ingredients.size)
    }

    @Test
    fun `update recipe with ingredients`() = runTestAsAdmin {
        val ingredient1 = it.createIngredient()
        val ingredient2 = it.createIngredient()
        val ingredient3 = it.createIngredient()

        val recipeIngredient1 = RecipeDTO.RecipeIngredientDTO(
            id = ingredient1.id,
            unit = AmountUnit.GRAM,
            amount = 1f,
        )

        val recipeIngredient2 = RecipeDTO.RecipeIngredientDTO(
            id = ingredient2.id,
            unit = AmountUnit.GRAM,
            amount = 1f,
        )
        val recipeIngredient2bis = RecipeDTO.RecipeIngredientDTO(
            id = ingredient2.id,
            unit = AmountUnit.UNIT,
            amount = 2f,
        )

        val recipeIngredient3 = RecipeDTO.RecipeIngredientDTO(
            id = ingredient3.id,
            unit = AmountUnit.GRAM,
            amount = 1f,
        )

        val expected = setOf(
            RecipeIngredientInfo(
                id = ingredient2.id,
                name = "",
                unit = AmountUnit.UNIT,
                amount = 2f,
            ),
            RecipeIngredientInfo(
                id = ingredient3.id,
                name = "",
                unit = AmountUnit.GRAM,
                amount = 1f,
            ),

        )

        val recipeDto = RecipeDTO(
            title = "My recipe",
            ingredients = mutableListOf(recipeIngredient1, recipeIngredient2)
        )
        val recipe = it.createRecipe(recipeDto)

        val recipeDTO2 = RecipeDTO(
            title = "My recipe",
            ingredients = mutableListOf(recipeIngredient2bis, recipeIngredient3)
        )

        val response = it.updateRecipe(recipe.id, recipeDTO2)
        it.assertRecipeExists(response.id)
        assertEquals(2, response.ingredients.size)
        assertEquals(expected, response.ingredients.toSet())
    }

    @Test
    fun `create recipe with steps`() = runTestAsAdmin {
        val steps = setOf(
            "step1",
            "step2",
            )
        val recipeDto = RecipeDTO(
            title = "My recipe",
            steps = steps.toMutableList()
        )
        val recipe = it.createRecipe(recipeDto)
        val response = it.getRecipe(recipe.id)
        it.assertRecipeExists(response.id)
        assertEquals(2, response.steps.size)
        assertEquals(steps, response.steps.toSet())
    }

    @Test
    fun `get recipe`() = runTestAsAdmin {
        val recipeInfo = it.createRecipe(recipeDTO)
        val result = it.getRecipe(recipeInfo.id)
        assertTrue(result.compareToDTO(recipeDTO))
    }

    @Test
    fun `update recipe`() = runTestAsAdmin {
        val response = it.createRecipe(recipeDTO)
        val recipeDTO2 = RecipeDTO(
            title = "My better recipe",
            description = "My new description",
            steps = mutableListOf(
                "slice",
                "cool",
            )
        )
        it.updateRecipe(response.id, recipeDTO2)
        assertFalse(it.getRecipe(response.id).compareToDTO(recipeDTO))
        assertTrue(it.getRecipe(response.id).compareToDTO(recipeDTO2))
    }

    @Test
    fun `updating unexisting recipe returns Unauthorized`() = runTestAsAdmin {
        it.put("$RECIPE_URL/-1"){
            contentType(ContentType.Application.Json)
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            setBody(recipeDTO)
        }.apply{
            assertEquals(HttpStatusCode.NotFound, status)
        }
    }

    @Test
    fun `delete recipe`() = runTestAsAdmin {
        val recipeInfo = it.createRecipe(recipeDTO)
        it.assertRecipeExists(recipeInfo.id)
        it.deleteRecipe(recipeInfo.id)
    }

    @Test
    fun `list recipes by owner`() = runTestAsAdmin {
        val admin = userService.getUserByUsername("admin")!!
        val user = it.createUser()
        val result = it.listRecipes(admin.id)
        assertEquals(result.count(), 0)
        it.createRecipe(recipeDTO)
        val result2 = it.listRecipes(admin.id)
        assertEquals(result2.count(), 1)
        val result3 = it.listRecipes(user.id)
        assertEquals(result3.count(), 0)
    }

    @Test
    fun `list recipes`() = runTestAsAdmin {
        val result = it.listRecipes(null)
        assertEquals(0, result.count())
        it.createRecipe(recipeDTO)
        val result2 = it.listRecipes(null)
        assertEquals(1, result2.count())
    }

    @Test
    fun `sort recipes by likes`() = runTestAsAdmin {
        val recipe1 = it.createRecipe()
        val recipe2 = it.createRecipe()
        it.createLike(recipe1.id)

        val result1 = it.listRecipes(null, Sort.LIKES_DESCENDING)
        assertEquals(2, result1.count())
        assertEquals(recipe1.id, result1[0].id)

        val result2 = it.listRecipes(null, Sort.LIKES_ASCENDING)
        assertEquals(2, result2.count())
        assertEquals(recipe2.id, result2[0].id)
    }

    @Test
    fun `sort recipes by date`() = runTestAsAdmin {
        val recipe1 = it.createRecipe()
        val recipe2 = it.createRecipe()
        val recipe3 = it.createRecipe()

        val dateOrderAscending = setOf(recipe1.id, recipe2.id, recipe3.id)
        val dateOrderDescending = setOf(recipe3.id, recipe2.id, recipe1.id)

        val result1 = it.listRecipes(null, Sort.DATE_DESCENDING)
        assertEquals(dateOrderDescending, result1.map {it.id}.toSet())

        val result2 = it.listRecipes(null, Sort.DATE_ASCENDING)
        assertEquals(dateOrderAscending, result2.map {it.id}.toSet())
    }
}