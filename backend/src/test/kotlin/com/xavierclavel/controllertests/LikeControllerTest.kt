package main.com.xavierclavel.controllertests

import com.xavierclavel.ApplicationTest
import com.xavierclavel.utils.logger
import main.com.xavierclavel.utils.assertLikeDoesNotExist
import main.com.xavierclavel.utils.assertLikeExists
import main.com.xavierclavel.utils.createLike
import main.com.xavierclavel.utils.createRecipe
import main.com.xavierclavel.utils.deleteLike
import main.com.xavierclavel.utils.getLikes
import main.com.xavierclavel.utils.getMe
import main.com.xavierclavel.utils.getRecipe
import main.com.xavierclavel.utils.getUser
import org.junit.Test
import kotlin.test.assertEquals

class LikeControllerTest : ApplicationTest() {
    @Test
    fun `create like`() = runTestAsAdmin {
        val userId = it.getMe().id
        val recipe = it.createRecipe()
        it.createLike(recipe.id)
        it.assertLikeExists(userId, recipe.id)
    }

    @Test
    fun `delete like`() = runTestAsAdmin {
        val userId = it.getMe().id
        val recipe = it.createRecipe()
        it.createLike(recipe.id)
        it.assertLikeExists(userId, recipe.id)
        logger.info { "create ok"}
        it.deleteLike(recipe.id)
        logger.info { "delete ok"}
        it.assertLikeDoesNotExist(userId, recipe.id)
        logger.info { "final check"}
    }

    @Test
    fun `list likes by user`() = runTestAsAdmin {
        val userId = it.getMe().id
        val recipe1 = it.createRecipe()
        val recipe2 = it.createRecipe()

        it.createLike(recipe1.id)
        it.createLike(recipe2.id)
        val result = it.getLikes(userId, null)
        assertEquals(2, result.size)
    }

    @Test
    fun `count likes per recipe`() = runTestAsAdmin {
        val recipeInfo = it.createRecipe()
        val result = it.getRecipe(recipeInfo.id)
        assertEquals(0, result.likesCount)
        it.createLike(result.id)
        val result2 = it.getRecipe(recipeInfo.id)
        assertEquals(1, result2.likesCount)
    }
}