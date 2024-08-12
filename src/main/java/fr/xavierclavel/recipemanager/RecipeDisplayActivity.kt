package fr.xavierclavel.recipemanager

import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.core.text.set


class RecipeDisplayActivity : ComponentActivity() {
    private lateinit var recipeTitle: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_recipe)

        recipeTitle = findViewById(R.id.recipeTitle)

        val b = intent.extras
        val value = b?.getInt("recipeId") ?: -1
        displayRecipe(value)
    }

    private fun displayRecipe(index: Int) {
        val recipe = DataManager.dictRecipes[index]
        recipeTitle.setText(recipe!!.title)
    }
}
