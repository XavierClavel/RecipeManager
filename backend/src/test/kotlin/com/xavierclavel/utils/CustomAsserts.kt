package main.com.xavierclavel.utils

import com.xavierclavel.TestBuilderWrapper
import com.xavierclavel.exceptions.NotFoundCause
import com.xavierclavel.exceptions.NotFoundException
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

inline fun <reified T : Throwable> assertException(message:String, executable: () -> Unit) {
    val exception = assertThrows<T> {
        executable()
    }
    assertEquals(exception.message, message)
}

suspend fun TestBuilderWrapper.assertRecipeDoesNotExist(id: Long) {
    assertException<NotFoundException>(NotFoundCause.RECIPE_NOT_FOUND.key) {
        client.getRecipe(id)
    }
}

suspend fun TestBuilderWrapper.assertRecipeExists(id: Long) {
    assertDoesNotThrow {
        client.getRecipe(id)
    }
}