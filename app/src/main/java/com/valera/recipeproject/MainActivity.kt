package com.valera.recipeproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.widget.ImageButton
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private lateinit var databaseHandler: DatabaseHandler
    private lateinit var allRecipes: List<RecipeModelClass>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnAdd = findViewById<ImageButton>(R.id.btnAdd)
        val btnView = findViewById<ImageButton>(R.id.btnViewAll)
        val btnFavorites = findViewById<ImageButton>(R.id.btnFavorites)

        val tvRecipeName = findViewById<TextView>(R.id.tvRecipeName)

        databaseHandler = DatabaseHandler(this)
        allRecipes = databaseHandler.viewRecipe()

        val featuredRecipeIndex = getFeaturedRecipe(allRecipes)
        val featuredRecipe: RecipeModelClass = allRecipes[featuredRecipeIndex]

        tvRecipeName.text = featuredRecipe.name

        btnAdd.setOnClickListener {
            startAddRecipe()
        }
        btnView.setOnClickListener {
            startViewRecipes()
        }
        btnFavorites.setOnClickListener {
            startFavoriteRecipes()
        }
    }

    /* Functions */
    private fun startAddRecipe() {
        val i = Intent(this, AddRecipe :: class.java)
        startActivity(i)
    }

    private fun startViewRecipes() {
        val i = Intent(this, ViewRecipes :: class.java)
        startActivity(i)
    }

    private fun startFavoriteRecipes() {
        val i = Intent(this, FavoriteRecipes :: class.java)
        startActivity(i)
    }

    private fun getFeaturedRecipe(allRecipes: List<RecipeModelClass>): Int {
        // Verify if there are recipes in the database
        return if (allRecipes.isNotEmpty()) {
            // Use random index to select featured recipe
            (allRecipes.indices).random()

        }
        else {
            -999
        }
    }
}