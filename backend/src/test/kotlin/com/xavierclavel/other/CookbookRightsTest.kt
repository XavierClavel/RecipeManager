package main.com.xavierclavel.other

import com.xavierclavel.ApplicationTest
import common.infodto.CookbookInfo
import common.infodto.RecipeInfo
import io.ktor.http.HttpStatusCode
import main.com.xavierclavel.utils.addCookbookRecipe
import main.com.xavierclavel.utils.createCookbook
import main.com.xavierclavel.utils.createRecipe
import main.com.xavierclavel.utils.deleteCookbookRecipe
import main.com.xavierclavel.utils.getRecipe
import main.com.xavierclavel.utils.recipeDTO
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class CookbookRightsTest: ApplicationTest() {

    @Test
    fun `user member of cookbook can add recipe`() = runTest{
        var recipe: RecipeInfo? = null
        var cookbook: CookbookInfo? = null
        runAsUser1 {
            recipe = client.createRecipe(recipeDTO)
            cookbook = client.createCookbook()
            client.addCookbookRecipe(cookbook.id, recipe.id).apply {
                assertEquals(status, HttpStatusCode.OK)
            }
        }
    }

    @Test
    fun `user not member of cookbook cannot add recipe`() = runTest{
        var recipe: RecipeInfo? = null
        var cookbook: CookbookInfo? = null
        runAsUser1 {
            cookbook = client.createCookbook()
        }
        runAsUser2 {
            recipe = client.createRecipe(recipeDTO)
            client.addCookbookRecipe(cookbook!!.id, recipe.id).apply {
                assertEquals(status, HttpStatusCode.Forbidden)
            }
        }
    }

    @Test
    fun `user member of cookbook can remove recipe`() = runTest{
        var recipe: RecipeInfo? = null
        var cookbook: CookbookInfo? = null
        runAsUser1 {
            recipe = client.createRecipe(recipeDTO)
            cookbook = client.createCookbook()
            client.addCookbookRecipe(cookbook.id, recipe.id)
            client.deleteCookbookRecipe(cookbook.id, recipe.id).apply {
                assertEquals(status, HttpStatusCode.OK)
            }
        }
    }

    @Test
    fun `user not member of cookbook cannot remove recipe`() = runTest{
        var recipe: RecipeInfo? = null
        var cookbook: CookbookInfo? = null
        runAsUser1 {
            cookbook = client.createCookbook()
            recipe = client.createRecipe(recipeDTO)
            client.addCookbookRecipe(cookbook.id, recipe.id)
        }
        runAsUser2 {
            client.deleteCookbookRecipe(cookbook!!.id, recipe!!.id).apply {
                assertEquals(status, HttpStatusCode.Forbidden)
            }
        }
    }

}