package com.xavierclavel.services

import com.xavierclavel.exceptions.NotFoundCause
import com.xavierclavel.exceptions.NotFoundException
import com.xavierclavel.models.jointables.RecipeNotes
import com.xavierclavel.models.jointables.query.QRecipeNotes
import com.xavierclavel.utils.DbTransaction.insertAndGet
import org.koin.core.component.KoinComponent
import org.koin.java.KoinJavaComponent.inject

class RecipeNotesService: KoinComponent {
    val recipeService by inject<RecipeService>(RecipeService::class.java)
    val userService by inject<UserService>(UserService::class.java)

    fun countAll(): Int =
        QRecipeNotes().findCount()


    fun findNotes(recipeId: Long, userId: Long): RecipeNotes =
        QRecipeNotes()
            .recipe.id.eq(recipeId)
            .user.id.eq(userId)
            .findOne()
            ?: throw NotFoundException(NotFoundCause.NOTES_NOT_FOUND)

    fun notesExists(recipeId: Long, userId: Long) =
        QRecipeNotes()
            .recipe.id.eq(recipeId)
            .user.id.eq(userId)
            .exists()

    fun createNotes(recipeId: Long, userId: Long, notes: String): String =
        RecipeNotes(
            recipe = recipeService.getEntityById(recipeId),
            user = userService.getEntityById(userId),
            notes = notes
        ).insertAndGet().notes

    fun updateNotes(recipeId: Long, userId: Long, notes: String): String =
        findNotes(recipeId, userId).apply {
            this.notes = notes
            this.save()
        }.notes

    fun deleteNotes(recipeId: Long, userId: Long) =
        findNotes(recipeId, userId).delete()

}