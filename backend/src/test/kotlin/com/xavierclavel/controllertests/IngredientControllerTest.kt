package main.com.xavierclavel.controllertests

import com.xavierclavel.ApplicationTest
import common.dto.IngredientDTO
import common.enums.IngredientType
import common.utils.URL.INGREDIENT_URL
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
import main.com.xavierclavel.utils.assertIngredientDoesNotExist
import main.com.xavierclavel.utils.assertIngredientExists
import main.com.xavierclavel.utils.createIngredient
import main.com.xavierclavel.utils.deleteIngredient
import main.com.xavierclavel.utils.getIngredient
import main.com.xavierclavel.utils.updateIngredient
import kotlin.test.assertFalse

class IngredientControllerTest : ApplicationTest() {
    @Test
    fun `create ingredient`() = runTestAsAdmin {
        val ingredientDTO = IngredientDTO(
            name = "My ingredient",
            type = IngredientType.CHEESE,
            calories = 10,
        )
        val response = it.createIngredient(ingredientDTO)
        it.assertIngredientExists(response.id)
    }

    @Test
    fun `get ingredient`() = runTestAsAdmin {
        val ingredientDTO = IngredientDTO(
            name = "My ingredient",
            type = IngredientType.CHEESE,
            calories = 10,
        )
        val ingredientInfo = it.createIngredient(ingredientDTO)
        val result = it.getIngredient(ingredientInfo.id)
        assertTrue(result.compareToDTO(ingredientDTO))
    }

    @Test
    fun `update ingredient`() = runTestAsAdmin {
        val ingredientDTO = IngredientDTO(
            name = "My ingredient",
            type = IngredientType.CHEESE,
            calories = 10,
        )
        val response = it.createIngredient(ingredientDTO)
        val ingredientDTO2 = IngredientDTO(
            name = "My better ingredient",
            type = IngredientType.VEGETABLE,
            calories = 10,
        )
        it.updateIngredient(response.id, ingredientDTO2)
        assertFalse(it.getIngredient(response.id).compareToDTO(ingredientDTO))
        assertTrue(it.getIngredient(response.id).compareToDTO(ingredientDTO2))
    }

    @Test
    fun `updating unexisting ingredient returns NotFound`() = runTestAsAdmin {
        val ingredientDTO = IngredientDTO(
            name = "My ingredient",
            type = IngredientType.CHEESE,
            calories = 10,
        )
        it.put("$INGREDIENT_URL/-1"){
            contentType(ContentType.Application.Json)
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            setBody(ingredientDTO)
        }.apply{
            assertEquals(HttpStatusCode.NotFound, status)
        }
    }

    @Test
    fun `delete ingredient`() = runTestAsAdmin {
        val ingredientDTO = IngredientDTO(
            name = "My ingredient",
            type = IngredientType.CHEESE,
            calories = 10,
        )
        val ingredientInfo = it.createIngredient(ingredientDTO)
        it.assertIngredientExists(ingredientInfo.id)
        it.deleteIngredient(ingredientInfo.id)
        it.assertIngredientDoesNotExist(ingredientInfo.id)
    }
}