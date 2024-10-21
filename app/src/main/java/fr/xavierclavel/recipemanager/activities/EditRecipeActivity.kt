package fr.xavierclavel.recipemanager.activities

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import fr.xavierclavel.recipemanager.DataManager
import fr.xavierclavel.recipemanager.IngredientType
import fr.xavierclavel.recipemanager.R
import fr.xavierclavel.recipemanager.Recipe
import fr.xavierclavel.recipemanager.WeightUnit


class EditRecipeActivity : AppCompatActivity() {
    private lateinit var recipeTitle: EditText
    private lateinit var recipePortions: EditText
    private lateinit var cookingTime: EditText
    private lateinit var saveButton: Button
    private lateinit var stepsLayout: LinearLayout
    private lateinit var addStepButton: Button
    private lateinit var addIngredientButton: Button
    private lateinit var ingredientsLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_recipe)

        saveButton = findViewById(R.id.saveButton)
        recipeTitle = findViewById(R.id.recipeTitle)
        stepsLayout = findViewById(R.id.stepsLayout)
        addStepButton = findViewById(R.id.addStepButton)
        cookingTime = findViewById(R.id.cookingTime)
        ingredientsLayout = findViewById(R.id.ingredientsLayout)
        addIngredientButton = findViewById(R.id.addIngredientButton)

        val b = intent.extras
        val recipeIndex = b?.getInt("recipeId") ?: -1
        if (recipeIndex != -1) recoverValues(recipeIndex)

        saveButton.setOnClickListener {
            DataManager.saveRecipe(getRecipeFromForm(), recipeIndex)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        addStepButton.setOnClickListener { addStep() }
        addIngredientButton.setOnClickListener { addIngredient() }
    }

    private fun addIngredient() {
        val linearLayout = LinearLayout(this)
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT,
        )

        val buttonLayout = LinearLayout(this)
        buttonLayout.layoutParams = layoutParams

        val amountText = EditText(this)
        amountText.inputType = EditorInfo.TYPE_CLASS_NUMBER
        amountText.hint = "Amount"
        buttonLayout.addView(amountText)

        val spinner = Spinner(this)
        spinner.adapter = ArrayAdapter<WeightUnit>(
            this,
            android.R.layout.simple_spinner_item,
            WeightUnit.entries.toTypedArray()
        )
        buttonLayout.addView(spinner)

        val ingredientText = EditText(this)
        ingredientText.hint = "Ingredient"
        buttonLayout.addView(ingredientText)

        val deleteButton = Button(this)

        ingredientsLayout.addView(linearLayout)

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
            preparationTime = null,
            cookingTime = cookingTime.text.toString().takeIf { it.isNotBlank() }?.toInt(),
            cookingTemperature = null,
            ingredients = mutableListOf(),
            steps = getSteps(),
            creationDate = 0,
            editionDate = 0
        )
    }

}