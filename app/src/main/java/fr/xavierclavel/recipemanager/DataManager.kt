package fr.xavierclavel.recipemanager

import android.util.Log
import fr.xavierclavel.recipemanager.activities.MainActivity
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileOutputStream
import java.time.Instant
import common.*

object DataManager {
    const val FILE_PREFIX = "recipe"
    var dictRecipes = hashMapOf<Int,Recipe>()

    fun saveRecipe(recipe: Recipe, index: Int = -1) {
        //index = -1 : new recipe
        val i = if (index != -1 ) index else findFirstAvailableIndex()
        val currentTime = Instant.now().epochSecond
        if (index == -1) recipe.creationDate = currentTime
        else recipe.editionDate = currentTime
        val filename = "${FILE_PREFIX}_${i}"
        val file = File(getRecipesDir(), filename)
        val deleted = file.delete()
        Log.d("deleted", "" + deleted)
        FileOutputStream(file).use {
            it.write(Json.encodeToString<Recipe>(recipe).encodeToByteArray())
        }
        getAllRecipes()
    }

    fun readRecipe(index: Int) : Recipe {
        val filename = "${FILE_PREFIX}_${index}"
        val file = File(getRecipesDir(), filename)
        return Json.decodeFromString<Recipe>(file.readText())
    }

    fun getAllRecipes() {
        dictRecipes = hashMapOf()
        val files = getRecipesDir().list()
        for (file in files) {
            val index = file.substringAfter("${FILE_PREFIX}_").toInt()
            dictRecipes[index] = readRecipe(index)
        }

    }

    fun deleteAll() {
        dictRecipes = hashMapOf()
        val files = getRecipesDir().list()
        for (file in files) {
            val file = File(getRecipesDir(), file)
            file.delete()
        }
    }

    private fun getRecipesDir() : File {
        val dir = File(MainActivity.instance.filesDir, "recipes")
        if (!dir.exists()) {
            dir.mkdir()
        }
        return dir
    }

    private fun findFirstAvailableIndex() : Int {
        var i = 0;
        while(dictRecipes.keys.contains(i)) {
            i++;
        }
        return i
    }
}