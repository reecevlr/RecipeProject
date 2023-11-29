package com.valera.recipeproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class FavoriteRecipes : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.favorite_recipes)

        val btnReturn = findViewById<Button>(R.id.btnReturn)

        btnReturn.setOnClickListener {
            startMainActivity()
        }
    }

    /* Functions */
    private fun startMainActivity() {
        val i = Intent(this, MainActivity :: class.java)
        startActivity(i)
    }

    private fun viewFavoriteRecipes() {
        val databaseHandler = DatabaseHandler(this)

        val recipe: List<RecipeModelClass> = databaseHandler.viewRecipe()
        val recipeArrayName = Array(recipe.size) {"null"}
        val recipeArrayFavorite = Array(recipe.size) {false}

        var index = 0

        for (r in recipe) {
            // Verify if recipe is favorite
            if (!r.favorite) {
                index++
            }
            else {
                recipeArrayName[index] = r.name
                recipeArrayFavorite[index] = r.favorite

                index++
            }
        }

        val listView =
            findViewById<ListView>(R.id.listView)
        val listAdapter =
            ListAdapter(this, recipeArrayName, recipeArrayFavorite)

        listView.adapter = listAdapter
    }
}