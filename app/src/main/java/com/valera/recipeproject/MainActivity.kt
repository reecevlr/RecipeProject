package com.valera.recipeproject

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    companion object {
        private const val KEY_FEATURED_RECIPE_INDEX = "featured_recipe_index"
    }

    private lateinit var databaseHandler: DatabaseHandler
    private lateinit var allRecipes: List<RecipeModelClass>

    private lateinit var tvRecipeName: TextView
    private lateinit var tvRecipeIngredients: TextView
    private lateinit var tvRecipeInstructions: TextView

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnAdd = findViewById<ImageButton>(R.id.btnAdd)
        val btnView = findViewById<ImageButton>(R.id.btnViewAll)
        val btnFavorites = findViewById<ImageButton>(R.id.btnFavorites)

        tvRecipeName = findViewById(R.id.tvRecipeName)
        tvRecipeIngredients = findViewById(R.id.tvRecipeIngredients)
        tvRecipeInstructions = findViewById(R.id.tvRecipeInstructions)

        databaseHandler = DatabaseHandler(this)
        allRecipes = databaseHandler.viewRecipe()

        // Saved Instance => Featured Recipe resets only on app launch
        if (savedInstanceState == null) {
            sharedPreferences = getPreferences(MODE_PRIVATE)

            val featuredRecipeIndex = generateRandomIndex()

            if (featuredRecipeIndex == -1) {
                tvRecipeName.text = buildString {
                    append("No recipes... Create some now!")
                }
            }
            else {
                saveFeaturedRecipeIndex(featuredRecipeIndex)
                displayFeaturedRecipe(allRecipes[featuredRecipeIndex])
            }
        }

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

    private fun generateRandomIndex(): Int {
        if (allRecipes.isEmpty()) {
            return -1
        }
        return (allRecipes.indices).random()
    }

    private fun saveFeaturedRecipeIndex(featuredRecipeIndex: Int) {
        val editor = sharedPreferences.edit()

        editor.putInt(KEY_FEATURED_RECIPE_INDEX, featuredRecipeIndex)
        editor.apply()
    }

    private fun displayFeaturedRecipe(featuredRecipe: RecipeModelClass) {
        tvRecipeName.text = featuredRecipe.name
        tvRecipeIngredients.text = featuredRecipe.ingredients.joinToString("\n")
        tvRecipeInstructions.text = featuredRecipe.instructions
    }
}