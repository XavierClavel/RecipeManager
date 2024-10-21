package fr.xavierclavel.recipemanager.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import fr.xavierclavel.recipemanager.DataManager
import fr.xavierclavel.recipemanager.R


class MainActivity : AppCompatActivity(), View.OnClickListener {
    companion object {
        lateinit var instance: MainActivity
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        instance = this
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("app", "start")
        findViewById<Button>(R.id.listButton).setOnClickListener(this)
        findViewById<Button>(R.id.editButton).setOnClickListener(this)
        findViewById<Button>(R.id.deleteAllButton).setOnClickListener(this)
    }

    override fun onClick(view: View) {
        Log.d("click",view.id.toString())
        when (view.id) {
            R.id.editButton -> {
                Log.d("click","edit")
                val intent = Intent(this, EditRecipeActivity::class.java)
                startActivity(intent)
            }
            R.id.listButton -> {
                Log.d("click","list")
                val intent = Intent(this, RecipeListActivity::class.java)
                startActivity(intent)
            }
            R.id.deleteAllButton -> {
                DataManager.deleteAll()
            }
        }
    }
}