package fr.xavierclavel.recipemanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText


class EditRecipeActivity : AppCompatActivity() {
    private lateinit var recipeTitle: EditText
    private lateinit var recipePortions: EditText
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_recipe)

        saveButton = findViewById(R.id.saveButton)
        recipeTitle = findViewById(R.id.recipeTitle)

        val b = intent.extras
        val recipeIndex = b?.getInt("recipeId") ?: -1
        if (recipeIndex != -1) recoverValues(recipeIndex)

        saveButton.setOnClickListener {
            DataManager.saveRecipe(getRecipeFromForm(), recipeIndex)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun recoverValues(recipeIndex: Int) {
        val recipe = DataManager.dictRecipes[recipeIndex]!!
        recipeTitle.setText(recipe.title)

    }

    private fun getRecipeFromForm() : Recipe {
        return Recipe(
            title = recipeTitle.text.toString(),
            portions = recipePortions.text.toString().toInt(),
        )
    }

}