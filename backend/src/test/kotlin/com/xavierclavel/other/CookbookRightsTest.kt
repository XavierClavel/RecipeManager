package main.com.xavierclavel.other

import com.xavierclavel.ApplicationTest
import common.dto.CookbookDTO
import common.enums.Visibility
import common.infodto.CookbookInfo
import common.infodto.RecipeInfo
import io.ktor.http.HttpStatusCode
import main.com.xavierclavel.utils.addCookbookRecipe
import main.com.xavierclavel.utils.addCookbookUser
import main.com.xavierclavel.utils.addCookbookUserRaw
import main.com.xavierclavel.utils.createCookbook
import main.com.xavierclavel.utils.createRecipe
import main.com.xavierclavel.utils.deleteCookbookRecipe
import main.com.xavierclavel.utils.deleteCookbookUser
import main.com.xavierclavel.utils.deleteCookbookUserRaw
import main.com.xavierclavel.utils.follow
import main.com.xavierclavel.utils.getCookbook
import main.com.xavierclavel.utils.getCookbookRaw
import main.com.xavierclavel.utils.listCookbooks
import main.com.xavierclavel.utils.recipeDTO
import org.junit.jupiter.api.Test
import org.koin.core.component.get
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

    @Test
    fun `admin of cookbook can add or remove users from cookbook`() = runTest{
        var cookbook: CookbookInfo? = null
        runAsUser1 {
            cookbook = client.createCookbook()
            client.addCookbookUser(cookbook.id, userService.getUserByUsername(USER2)!!.id, false)
            client.deleteCookbookUser(cookbook.id, userService.getUserByUsername(USER2)!!.id)
        }
    }

    @Test
    fun `non admin of cookbook cannot add or remove users from cookbook`() = runTest{
        var cookbook: CookbookInfo? = null
        runAsUser1 {
            cookbook = client.createCookbook()
        }
        runAsUser2 {
            client.addCookbookUserRaw(cookbook!!.id, userService.getUserByUsername(USER2)!!.id, false).apply {
                assertEquals(status, HttpStatusCode.Forbidden)
            }
            client.deleteCookbookUserRaw(cookbook.id, userService.getUserByUsername(USER2)!!.id).apply {
                assertEquals(status, HttpStatusCode.Forbidden)
            }
        }
    }

    @Test
    fun `non members of cookbook can see public cookbook`() = runTest{
        var cookbook: CookbookInfo? = null
        runAsUser1 {
            cookbook = client.createCookbook(CookbookDTO("", visibility = Visibility.PUBLIC))
        }
        runAsUser2 {
            client.getCookbook(cookbook!!.id)
            val result = client.listCookbooks()
            assertEquals(1, result.size)
        }
    }

    @Test
    fun `non members of cookbook cannot see protected cookbook`() = runTest{
        var cookbook: CookbookInfo? = null
        runAsUser1 {
            cookbook = client.createCookbook(CookbookDTO("", visibility = Visibility.PROTECTED))
        }
        runAsUser2 {
            client.getCookbookRaw(cookbook!!.id).apply {
                assertEquals(HttpStatusCode.Forbidden, status)
            }
            val result = client.listCookbooks()
            assertEquals(0, result.size)
        }
    }

    @Test
    fun `non members of cookbook cannot see private cookbook`() = runTest{
        var cookbook: CookbookInfo? = null
        runAsUser1 {
            cookbook = client.createCookbook(CookbookDTO("", visibility = Visibility.PRIVATE))
        }
        runAsUser2 {
            client.getCookbookRaw(cookbook!!.id).apply {
                assertEquals(HttpStatusCode.Forbidden, status)
            }
            val result = client.listCookbooks()
            assertEquals(0, result.size)
        }
    }

    @Test
    fun `followers of cookbook members can see public cookbook`() = runTest{
        var cookbook: CookbookInfo? = null
        runAsUser1 {
            cookbook = client.createCookbook(CookbookDTO("", visibility = Visibility.PUBLIC))
        }
        runAsUser2 {
            client.follow(userService.getUserByUsername(USER1)!!.id)
            client.getCookbook(cookbook!!.id)
            val result = client.listCookbooks()
            assertEquals(1, result.size)
        }
    }

    @Test
    fun `followers of cookbook members can see protected cookbook`() = runTest{
        var cookbook: CookbookInfo? = null
        runAsUser1 {
            cookbook = client.createCookbook(CookbookDTO("", visibility = Visibility.PROTECTED))
        }
        runAsUser2 {
            client.follow(userService.getUserByUsername(USER1)!!.id)
            client.getCookbook(cookbook!!.id)
            val result = client.listCookbooks()
            assertEquals(1, result.size)
        }
    }

    @Test
    fun `followers of cookbook members cannot see private cookbook`() = runTest{
        var cookbook: CookbookInfo? = null
        runAsUser1 {
            cookbook = client.createCookbook(CookbookDTO("", visibility = Visibility.PRIVATE))
        }
        runAsUser2 {
            client.follow(userService.getUserByUsername(USER1)!!.id)
            client.getCookbookRaw(cookbook!!.id).apply {
                assertEquals(HttpStatusCode.Forbidden, status)
            }
            val result = client.listCookbooks()
            assertEquals(0, result.size)
        }
    }

    @Test
    fun `members of cookbook can see public cookbook`() = runTest{
        var cookbook: CookbookInfo? = null
        runAsUser1 {
            cookbook = client.createCookbook(CookbookDTO("", visibility = Visibility.PUBLIC))
            client.addCookbookUser(cookbook.id, userService.getUserByUsername(USER2)!!.id, false)
        }
        runAsUser2 {
            client.getCookbook(cookbook!!.id)
            val result = client.listCookbooks()
            assertEquals(1, result.size)
        }
    }

    @Test
    fun `members of cookbook can see protected cookbook`() = runTest{
        var cookbook: CookbookInfo? = null
        runAsUser1 {
            cookbook = client.createCookbook(CookbookDTO("", visibility = Visibility.PROTECTED))
            client.addCookbookUser(cookbook.id, userService.getUserByUsername(USER2)!!.id, false)
        }
        runAsUser2 {
            client.getCookbook(cookbook!!.id)
            val result = client.listCookbooks()
            assertEquals(1, result.size)
        }
    }

    @Test
    fun `members of cookbook can see private cookbook`() = runTest{
        var cookbook: CookbookInfo? = null
        runAsUser1 {
            cookbook = client.createCookbook(CookbookDTO("", visibility = Visibility.PRIVATE))
            client.addCookbookUser(cookbook.id, userService.getUserByUsername(USER2)!!.id, false)
        }
        runAsUser2 {
            client.getCookbook(cookbook!!.id)
            val result = client.listCookbooks()
            assertEquals(1, result.size)
        }
    }

}