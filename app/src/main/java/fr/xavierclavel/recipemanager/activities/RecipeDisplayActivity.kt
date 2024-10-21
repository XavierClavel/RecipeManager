package fr.xavierclavel.recipemanager.activities

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import fr.xavierclavel.recipemanager.DataManager
import fr.xavierclavel.recipemanager.R
import java.text.SimpleDateFormat
import java.util.Locale


class RecipeDisplayActivity : ComponentActivity() {
    private lateinit var recipeTitle: TextView
    private lateinit var creationDate: TextView
    private lateinit var editionDate: TextView
    private lateinit var recipePortions: TextView
    private lateinit var editButton: Button
    private lateinit var shareButton: Button
    private lateinit var stepsLayout: LinearLayout

    private val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_recipe)

        recipeTitle = findViewById(R.id.recipeTitle)
        creationDate = findViewById(R.id.creationDate)
        editionDate = findViewById(R.id.editionDate)
        editButton = findViewById(R.id.editButton)
        shareButton = findViewById(R.id.shareButton)
        stepsLayout = findViewById(R.id.stepsLayout)

        val b = intent.extras
        val recipeIndex = b?.getInt("recipeId") ?: -1
        displayRecipe(recipeIndex)

        editButton.setOnClickListener{
            val intent = Intent(this, EditRecipeActivity::class.java)
            val b = Bundle()
            b.putInt("recipeId", recipeIndex) //Your id
            intent.putExtras(b) //Put your id to your next Intent
            startActivity(intent)
            finish()
        }
    }

    private fun displayRecipe(index: Int) {
        val recipe = DataManager.dictRecipes[index]!!
        recipeTitle.text = recipe.title
        creationDate.text = simpleDateFormat.format(recipe.creationDate * 1000L)
        if (recipe.editionDate == null) editionDate.text = "" else editionDate.text = simpleDateFormat.format(
            recipe.editionDate!! * 1000L)
        for (step in recipe.steps) {
            addStep(step)
        }
    }

    private fun addStep(text: String = "") {
        val textView = TextView(this)
        textView.inputType = InputType.TYPE_TEXT_FLAG_MULTI_LINE
        textView.text = text
        stepsLayout.addView(textView)
    }
}
