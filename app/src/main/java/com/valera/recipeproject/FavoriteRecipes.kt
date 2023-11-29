package com.valera.recipeproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class FavoriteRecipes : AppCompatActivity() {

    private lateinit var listAdapter: ListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.favorite_recipes)

        viewFavoriteRecipes()

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

        val allRecipes: List<RecipeModelClass> = databaseHandler.viewRecipe()

        // Filter recipes by favorite only
        val favoriteRecipes = allRecipes.filter { it.favorite }

        val recipeArrayId = favoriteRecipes.map { it.id ?: 0}.toTypedArray()
        val recipeArrayName = favoriteRecipes.map { it.name }.toTypedArray()
        val recipeArrayFavorite = favoriteRecipes.map { it.favorite }.toTypedArray()

        val listView = findViewById<ListView>(R.id.listView)

        listAdapter = ListAdapter(this, recipeArrayName, recipeArrayFavorite) { position ->
            // Handles favorite click
            val recipeId = recipeArrayId[position]
            val newFavoriteStatus = !recipeArrayFavorite[position]

            // Updates favorite status in database
            databaseHandler.updateFavoriteStatus(recipeId, newFavoriteStatus)

            // Reflect changes to array
            recipeArrayFavorite[position] = newFavoriteStatus
            listAdapter.notifyDataSetChanged()

            // Reflect changes to listView in real-time
            if (!newFavoriteStatus) {
                viewFavoriteRecipes()
            }
        }

        listView.adapter = listAdapter
    }
}