package main.com.xavierclavel.controllertests

import com.xavierclavel.ApplicationTest
import common.dto.RecipeDTO
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
import main.com.xavierclavel.utils.createRecipe
import main.com.xavierclavel.utils.deleteRecipe
import main.com.xavierclavel.utils.getRecipe
import main.com.xavierclavel.utils.updateRecipe
import kotlin.test.assertFalse

class RecipeControllerTest : ApplicationTest() {
    @Test
    fun `create recipe`() = runTestAsAdmin {
        val recipeDTO = RecipeDTO(
            title = "My recipe",
            description = "My description",
            steps = mutableListOf(
                "cut",
                "cook"
            )
        )
        val response = it.createRecipe(recipeDTO)
        it.assertRecipeExists(response.id)
    }

    @Test
    fun `get recipe`() = runTestAsAdmin {
        val recipeDTO = RecipeDTO(
            title = "My recipe",
            description = "My description",
            steps = mutableListOf(
                "cut",
                "cook"
            )
        )
        val recipeInfo = it.createRecipe(recipeDTO)
        val result = it.getRecipe(recipeInfo.id)
        assertTrue(result.compareToDTO(recipeDTO))
    }

    @Test
    fun `update recipe`() = runTestAsAdmin {
        val recipeDTO = RecipeDTO(
            title = "My recipe",
            description = "My description",
            steps = mutableListOf(
                "cut",
                "cook"
            )
        )
        val response = it.createRecipe(recipeDTO)
        val recipeDTO2 = RecipeDTO(
            title = "My better recipe",
            description = "My new description",
            steps = mutableListOf(
                "slice",
                "cool"
            )
        )
        it.updateRecipe(response.id, recipeDTO2)
        assertFalse(it.getRecipe(response.id).compareToDTO(recipeDTO))
        assertTrue(it.getRecipe(response.id).compareToDTO(recipeDTO2))
    }

    @Test
    fun `updating unexisting recipe returns Unauthorized`() = runTestAsAdmin {
        val recipeDTO = RecipeDTO(
            title = "My recipe",
            description = "My description",
            steps = mutableListOf(
                "cut",
                "cook"
            )
        )
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
        val recipeDTO = RecipeDTO(
            title = "My recipe",
            description = "My description",
            steps = mutableListOf(
                "cut",
                "cook"
            )
        )
        val recipeInfo = it.createRecipe(recipeDTO)
        it.assertRecipeExists(recipeInfo.id)
        it.deleteRecipe(recipeInfo.id)

    }
}