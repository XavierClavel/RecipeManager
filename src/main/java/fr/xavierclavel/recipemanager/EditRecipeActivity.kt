package fr.xavierclavel.recipemanager

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity


class EditRecipeActivity : AppCompatActivity() {
    private lateinit var recipeTitle: EditText
    private lateinit var recipePortions: EditText
    private lateinit var saveButton: Button
    private lateinit var stepsLayout: LinearLayout
    private lateinit var addStepButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_recipe)

        saveButton = findViewById(R.id.saveButton)
        recipeTitle = findViewById(R.id.recipeTitle)
        stepsLayout = findViewById(R.id.stepsLayout)
        addStepButton = findViewById(R.id.addStepButton)

        val b = intent.extras
        val recipeIndex = b?.getInt("recipeId") ?: -1
        if (recipeIndex != -1) recoverValues(recipeIndex)

        saveButton.setOnClickListener {
            DataManager.saveRecipe(getRecipeFromForm(), recipeIndex)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        addStepButton.setOnClickListener { addStep() }
    }

    private fun addStep(text: String = "") {
        val editText = EditText(this)
        editText.inputType = InputType.TYPE_TEXT_FLAG_MULTI_LINE
        editText.setText(text)
        stepsLayout.addView(editText)
    }

    private fun getSteps() : MutableList<String> {
        val steps = mutableListOf<String>()
        for (index in 0 until stepsLayout.childCount) {
            val step = stepsLayout.getChildAt(index) as EditText
            if (step.text != null) steps.add(step.text.trim().toString())
        }
        return steps
    }

    private fun recoverValues(recipeIndex: Int) {
        val recipe = DataManager.dictRecipes[recipeIndex]!!
        recipeTitle.setText(recipe.title)
        if (recipe.steps.isNotEmpty()) stepsLayout.removeAllViews()
        recipe.steps.forEach { addStep(it) }
    }

    private fun getRecipeFromForm() : Recipe {
        return Recipe(
            title = recipeTitle.text.toString(),
            portions = 1,//recipePortions.text.toString().toInt(),
            getSteps(),
        )
    }

}