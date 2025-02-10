package main.com.xavierclavel.controllertests

import com.xavierclavel.ApplicationTest
import com.xavierclavel.utils.logger
import main.com.xavierclavel.utils.assertLikeDoesNotExist
import main.com.xavierclavel.utils.assertLikeExists
import main.com.xavierclavel.utils.createLike
import main.com.xavierclavel.utils.createRecipe
import main.com.xavierclavel.utils.createUser
import main.com.xavierclavel.utils.deleteLike
import main.com.xavierclavel.utils.getLikes
import main.com.xavierclavel.utils.getMe
import main.com.xavierclavel.utils.getRecipe
import main.com.xavierclavel.utils.getUser
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class LikeControllerTest : ApplicationTest() {
    @Test
    fun `create like`() = runTestAsAdmin {
        val userId = client.getMe().id
        val recipe = client.createRecipe()
        client.createLike(recipe.id)
        client.assertLikeExists(userId, recipe.id)
    }

    @Test
    fun `delete like`() = runTestAsAdmin {
        val userId = client.getMe().id
        val recipe = client.createRecipe()
        client.createLike(recipe.id)
        client.assertLikeExists(userId, recipe.id)
        logger.info { "create ok"}
        client.deleteLike(recipe.id)
        logger.info { "delete ok"}
        client.assertLikeDoesNotExist(userId, recipe.id)
        logger.info { "final check"}
    }

    @Test
    fun `list likes by user`() = runTestAsAdmin {
        val userId = client.getMe().id
        val recipe1 = client.createRecipe()
        val recipe2 = client.createRecipe()

        client.createLike(recipe1.id)
        client.createLike(recipe2.id)
        val result = client.getLikes(userId, null)
        assertEquals(2, result.size)
    }

    @Test
    fun `count likes per recipe`() = runTestAsAdmin {
        val recipeInfo = client.createRecipe()
        val result = client.getRecipe(recipeInfo.id)
        assertEquals(0, result.likesCount)
        client.createLike(result.id)
        val result2 = client.getRecipe(recipeInfo.id)
        assertEquals(1, result2.likesCount)
    }

    @Test
    fun `count likes per user`() = runTestAsAdmin {
        val recipeInfo = client.createRecipe()
        val user = userService.getUserByUsername("admin")!!
        val user2 = client.createUser()

        val result = client.getUser(user.id)
        assertEquals(0, result.likesCount)

        client.createLike(recipeInfo.id)
        val result2 = client.getUser(user.id)
        assertEquals(1, result2.likesCount)

        val result3 = client.getUser(user2.id)
        assertEquals(0, result3.likesCount)

        client.deleteLike(recipeInfo.id)
        val result4 = client.getUser(user.id)
        assertEquals(0, result4.likesCount)
    }
}