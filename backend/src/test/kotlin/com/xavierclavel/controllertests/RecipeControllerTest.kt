package main.com.xavierclavel.controllertests

import com.xavierclavel.ApplicationTest
import com.xavierclavel.utils.logger
import common.dto.RecipeDTO
import common.dto.RecipeDTO.RecipeIngredientDTO
import common.enums.AmountUnit
import common.enums.IngredientType
import common.enums.Sort
import common.infodto.RecipeInfo
import common.infodto.RecipeIngredientInfo
import common.utils.URL.RECIPE_URL
import io.ktor.client.request.header
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import io.ktor.client.request.put
import main.com.xavierclavel.utils.assertRecipeDoesNotExist
import main.com.xavierclavel.utils.assertRecipeExists
import main.com.xavierclavel.utils.createIngredient
import main.com.xavierclavel.utils.createLike
import main.com.xavierclavel.utils.createRecipe
import main.com.xavierclavel.utils.createUser
import main.com.xavierclavel.utils.deleteLike
import main.com.xavierclavel.utils.deleteRecipe
import main.com.xavierclavel.utils.getMe
import main.com.xavierclavel.utils.getRecipe
import main.com.xavierclavel.utils.listRecipes
import main.com.xavierclavel.utils.updateRecipe
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.assertDoesNotThrow
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
        val response = client.createRecipe(recipeDTO)
        client.assertRecipeExists(response.id)
    }

    @Test
    fun `create recipe with ingredients`() = runTestAsAdmin {
        val ingredient1 = client.createIngredient()
        val ingredient2 = client.createIngredient()

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
        val recipe = client.createRecipe(recipeDto)
        val response = client.getRecipe(recipe.id)
        client.assertRecipeExists(response.id)
        assertEquals(2, response.ingredients.size)
    }

    @Test
    fun `update recipe ingredients`() = runTestAsAdmin {
        val user = client.getMe()

        val ingredient1 = client.createIngredient()
        val ingredient2 = client.createIngredient()
        val ingredient3 = client.createIngredient()

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
                complement = null,
                type = IngredientType.VEGETABLE,
            ),
            RecipeIngredientInfo(
                id = ingredient3.id,
                name = "",
                unit = AmountUnit.GRAM,
                amount = 1f,
                complement = null,
                type = IngredientType.VEGETABLE
            ),

        )

        val recipeDto = RecipeDTO(
            title = "My recipe",
            ingredients = mutableListOf(recipeIngredient1, recipeIngredient2)
        )
        val recipe = client.createRecipe(recipeDto)

        val recipeDTO2 = RecipeDTO(
            title = "My recipe",
            ingredients = mutableListOf(recipeIngredient2bis, recipeIngredient3)
        )

        val response = client.updateRecipe(recipe.id, recipeDTO2)
        client.assertRecipeExists(response.id)
        assertEquals(2, response.ingredients.size)
        assertEquals(expected, response.ingredients.toSet())
    }

    @Test
    fun `create recipe with custom ingredients`() = runTestAsAdmin {
        val customIngredient1 = RecipeDTO.CustomIngredientDTO(
            name = "custom ingredient 1",
            unit = AmountUnit.GRAM,
            amount = 1f,
        )

        val customIngredient2 = RecipeDTO.CustomIngredientDTO(
            name = "custom ingredient 2",
            unit = AmountUnit.GRAM,
            amount = 1f,
        )
        val recipeDto = RecipeDTO(
            title = "My recipe",
            customIngredients = mutableListOf(customIngredient1, customIngredient2)
        )
        val recipe = client.createRecipe(recipeDto)
        val response = client.getRecipe(recipe.id)
        client.assertRecipeExists(response.id)
        assertEquals(2, response.customIngredients.size)
    }

    @Test
    fun `update recipe custom ingredients`() = runTestAsAdmin {
        val customIngredient1 = RecipeDTO.CustomIngredientDTO(
            name = "custom ingredient 1",
            unit = AmountUnit.GRAM,
            amount = 1f,
        )

        val customIngredient2 = RecipeDTO.CustomIngredientDTO(
            name = "custom ingredient 2",
            unit = AmountUnit.GRAM,
            amount = 1f,
        )
        val customIngredient2bis = RecipeDTO.CustomIngredientDTO(
            name = "custom ingredient 2",
            unit = AmountUnit.UNIT,
            amount = 5f,
        )

        val customIngredient3 = RecipeDTO.CustomIngredientDTO(
            name = "custom ingredient 3",
            unit = AmountUnit.GRAM,
            amount = 1f,
        )

        val recipeDto1 = RecipeDTO(
            title = "My recipe",
            customIngredients = mutableListOf(customIngredient1, customIngredient2)
        )
        val recipeDto2 = RecipeDTO(
            title = "My recipe",
            customIngredients = mutableListOf(customIngredient2bis, customIngredient3)
        )

        val expected1 = setOf(
            customIngredient1.toInfo(),
            customIngredient2.toInfo(),
        )

        val expected2 = setOf(
            customIngredient2bis.toInfo(),
            customIngredient3.toInfo(),
        )

        val recipe = client.createRecipe(recipeDto1)
        val response = client.getRecipe(recipe.id)
        assertEquals(expected1, response.customIngredients.toSet())

        client.updateRecipe(recipe.id, recipeDto2)
        val response2 = client.getRecipe(recipe.id)
        assertEquals(expected2, response2.customIngredients.toSet())
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
        val response = client.createRecipe(recipeDto)
        client.assertRecipeExists(response.id)
        assertEquals(2, response.steps.size)
        assertEquals(steps, response.steps.toSet())
    }

    @Test
    fun `update recipe steps`() = runTestAsAdmin {
        val steps1 = setOf(
            "step1",
            "step2",
        )
        val recipeDto = RecipeDTO(
            title = "My recipe",
            steps = steps1.toMutableList()
        )

        val steps2 = setOf(
            "step2",
            "step3",
            "step4",
        )
        val recipeDto2 = RecipeDTO(
            title = "My recipe",
            steps = steps2.toMutableList()
        )

        val response = client.createRecipe(recipeDto)
        assertEquals(steps1, response.steps.toSet())

        val response2 = client.updateRecipe(response.id, recipeDto2)
        assertEquals(steps2, response2.steps.toSet())
    }



    @Test
    fun `get recipe`() = runTestAsAdmin {
        val recipeInfo = client.createRecipe(recipeDTO)
        val result = client.getRecipe(recipeInfo.id)
        assertTrue(result.compareToDTO(recipeDTO))
    }

    @Test
    fun `update recipe`() = runTestAsAdmin {
        val response = client.createRecipe(recipeDTO)
        val recipeDTO2 = RecipeDTO(
            title = "My better recipe",
            description = "My new description",
            steps = mutableListOf(
                "slice",
                "cool",
            )
        )
        client.updateRecipe(response.id, recipeDTO2)
        assertFalse(client.getRecipe(response.id).compareToDTO(recipeDTO))
        assertTrue(client.getRecipe(response.id).compareToDTO(recipeDTO2))
    }

    @Test
    fun `updating unexisting recipe returns Unauthorized`() = runTestAsAdmin {
        client.put("$RECIPE_URL/-1"){
            contentType(ContentType.Application.Json)
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            setBody(recipeDTO)
        }.apply{
            assertEquals(HttpStatusCode.NotFound, status)
        }
    }

    @Test
    fun `delete recipe`() = runTestAsAdmin {
        val recipeInfo = client.createRecipe(recipeDTO)
        client.assertRecipeExists(recipeInfo.id)
        client.deleteRecipe(recipeInfo.id)
    }

    @Test
    fun `list recipes by owner`() = runTestAsAdmin {
        val admin = userService.getUserByUsername("admin")!!
        val user = client.createUser()
        val result = client.listRecipes(admin.id)
        assertEquals(result.count(), 0)
        client.createRecipe(recipeDTO)
        val result2 = client.listRecipes(admin.id)
        assertEquals(result2.count(), 1)
        val result3 = client.listRecipes(user.id)
        assertEquals(result3.count(), 0)
    }

    @Test
    fun `list recipes`() = runTestAsAdmin {
        val user = client.getMe()
        val result = client.listRecipes(user = user.id)
        assertEquals(0, result.count())
        client.createRecipe(recipeDTO)
        val result2 = client.listRecipes(user = user.id)
        assertEquals(1, result2.count())
    }

    @Test
    fun `sort recipes by likes`() = runTestAsAdmin {
        val user = client.getMe()

        val recipe1 = client.createRecipe()
        val recipe2 = client.createRecipe()
        client.createLike(recipe1.id)

        val result1 = client.listRecipes(user = user.id, Sort.LIKES_DESCENDING)
        assertEquals(2, result1.count())
        assertEquals(recipe1.id, result1[0].id)

        val result2 = client.listRecipes(user = user.id, Sort.LIKES_ASCENDING)
        assertEquals(2, result2.count())
        assertEquals(recipe2.id, result2[0].id)
    }

    @Test
    fun `sort recipes by date`() = runTestAsAdmin {
        val user = client.getMe()

        val recipe1 = client.createRecipe()
        val recipe2 = client.createRecipe()
        val recipe3 = client.createRecipe()

        val dateOrderAscending = setOf(recipe1.id, recipe2.id, recipe3.id)
        val dateOrderDescending = setOf(recipe3.id, recipe2.id, recipe1.id)

        val result1 = client.listRecipes(user=user.id, Sort.DATE_DESCENDING)
        assertEquals(dateOrderDescending, result1.map {it.id}.toSet())

        val result2 = client.listRecipes(user=user.id, Sort.DATE_ASCENDING)
        assertEquals(dateOrderAscending, result2.map {it.id}.toSet())
    }

    @Test
    fun `recipes with no links are deleted`() = runTestAsUser {
        val recipe = client.createRecipe(recipeDTO)
        assertDoesNotThrow {
            client.getRecipe(recipe.id)
        }
        client.deleteRecipe(recipe.id)
        client.assertRecipeDoesNotExist(recipe.id)
    }

    @Test
    fun `recipes with links are not deleted`() = runTest {
        var recipe: RecipeInfo? = null
        runAsUser1 {
            recipe = client.createRecipe()
            val a = client.getRecipe(recipe.id)
            logger.info {a}
        }
        recipe!!
        runAsUser2 {
            client.createLike(recipe.id)
        }
        runAsUser1 {
            client.deleteRecipe(recipe.id)
        }
        runAsUser2 {
            client.assertRecipeExists(recipe.id)
        }

    }

    @Test
    fun `recipes with links are deleted once links are removed`() = runTestAsAdmin {
        var recipe: RecipeInfo? = null
        runAsUser1 {
            recipe = client.createRecipe()
        }
        recipe!!
        runAsUser2 {
            client.createLike(recipe.id)
        }
        runAsUser1 {
            client.deleteRecipe(recipe.id)
        }
        runAsUser2 {
            client.assertRecipeExists(recipe.id)
            client.deleteLike(recipe.id)
            client.assertRecipeDoesNotExist(recipe.id)
        }
    }

    @Test
    fun `recipes tagged for deletion are not shown to recipe owner`() = runTestAsAdmin {
        var recipe: RecipeInfo? = null
        runAsUser1 {
            recipe = client.createRecipe()
        }
        recipe!!
        runAsUser2 {
            client.createLike(recipe.id)
        }
        runAsUser1 {
            client.deleteRecipe(recipe.id)
            client.assertRecipeDoesNotExist(recipe.id)
        }
        runAsUser2 {
            client.assertRecipeExists(recipe.id)
        }
    }

}