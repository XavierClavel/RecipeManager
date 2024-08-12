package fr.xavierclavel.recipemanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText


class EditRecipeActivity : AppCompatActivity(), View.OnClickListener {
    companion object {
        lateinit var instance: EditRecipeActivity
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        instance = this
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_recipe)
        findViewById<Button>(R.id.saveButton).setOnClickListener(this)
    }

    private fun getRecipeFromForm() : Recipe {
        val recipeTitle = findViewById<EditText>(R.id.recipeTitle).text.toString()
        return Recipe(
            recipeTitle
        )
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.saveButton -> {
                DataManager.saveRecipe(getRecipeFromForm())
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }
}