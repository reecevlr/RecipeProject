package com.valera.recipeproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class ViewRecipes : AppCompatActivity() {

    private lateinit var listAdapter: ListAdapter
    private lateinit var recipeArrayId: Array<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_recipes)

        viewRecipes()

        val btnReturn = findViewById<Button>(R.id.btnReturn)
        val listView = findViewById<ListView>(R.id.listView)

        btnReturn.setOnClickListener {
            startMainActivity()
        }

        // Set an OnItemClickListener for the ListView
        listView.setOnItemClickListener { _, _, position, _ ->
            // Get the recipe ID based on the clicked position
            val recipeId = recipeArrayId[position]

            val intent = Intent(this, ViewRecipeDetails::class.java)
            intent.putExtra("recipeId", recipeId)

            startActivity(intent)
        }
    }

    /* Functions */
    private fun startMainActivity() {
        val i = Intent(this, MainActivity :: class.java)
        startActivity(i)
    }

    private fun viewRecipes() {
        val databaseHandler = DatabaseHandler(this)

        val recipe: List<RecipeModelClass> = databaseHandler.viewRecipe()
        recipeArrayId = Array(recipe.size) {0}
        val recipeArrayName = Array(recipe.size) {"null"}
        val recipeArrayFavorite = Array(recipe.size) {false}

        var index = 0

        for (r in recipe) {
            recipeArrayId[index] = r.id!!
            recipeArrayName[index] = r.name
            recipeArrayFavorite[index] = r.favorite

            index++
        }

        val listView =
            findViewById<ListView>(R.id.listView)
        listAdapter =
            ListAdapter(this, recipeArrayName, recipeArrayFavorite) { position ->
                // Handles favorite click
                val recipeId = recipeArrayId[position]
                val newFavoriteStatus = !recipeArrayFavorite[position]

                // Update favorite status in database
                databaseHandler.updateFavoriteStatus(recipeId, newFavoriteStatus)

                // Reflect changes to array
                recipeArrayFavorite[position] = newFavoriteStatus
                listAdapter.notifyDataSetChanged()
            }

        listView.adapter = listAdapter
    }
}
