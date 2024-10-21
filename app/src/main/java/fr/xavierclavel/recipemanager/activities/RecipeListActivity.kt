package fr.xavierclavel.recipemanager.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.ComponentActivity
import fr.xavierclavel.recipemanager.DataManager
import fr.xavierclavel.recipemanager.R

class RecipeListActivity : ComponentActivity() {
    private lateinit var linLayout : LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_recipes)

        linLayout = findViewById(R.id.scrollLayout)

        if (DataManager.dictRecipes.isEmpty()) DataManager.getAllRecipes()

        for (recipe in DataManager.dictRecipes.keys) {
            Log.d("recipeList", recipe.toString())
            addRecipeView(recipe)
        }
        Log.d("recipeList", "complete")
    }

    private fun addRecipeView(index: Int) {
        val button = Button(this)
        button.text = DataManager.dictRecipes[index]!!.title

        val buttonParam = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT,
        )

        val buttonLayout = LinearLayout(this)
        buttonLayout.layoutParams = buttonParam

        button.setOnClickListener{
            Log.d("click", "display recipe")
            val intent = Intent(this, RecipeDisplayActivity::class.java)
            val b = Bundle()
            b.putInt("recipeId", index) //Your id
            intent.putExtras(b) //Put your id to your next Intent
            startActivity(intent)
            finish()
        }

        buttonLayout.addView(button)
        linLayout.addView(buttonLayout)
    }
}
