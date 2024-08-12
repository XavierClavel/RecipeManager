package fr.xavierclavel.recipemanager

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.activity.ComponentActivity
import java.io.File

class RecipeListActivity : ComponentActivity() {
    private lateinit var linLayout : LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_recipes)

        linLayout = findViewById(R.id.scrollLayout)

        for (recipe in DataManager.dictRecipes.keys) {
            Log.d("recipeList", recipe.toString())
            addRecipeView(recipe)
        }
        Log.d("recipeList", "complete")
    }

    private fun addRecipeView(index: Int) {
        val button = ImageButton(this)
        linLayout.addView(button)
        button.setOnClickListener{ View.OnClickListener {
            val intent = Intent(this, RecipeDisplayActivity::class.java)
            val b = Bundle()
            b.putInt("recipeId", index) //Your id
            intent.putExtras(b) //Put your id to your next Intent
            startActivity(intent)
            finish()
        }}
    }
}
