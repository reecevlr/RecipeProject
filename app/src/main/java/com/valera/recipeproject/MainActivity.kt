package com.valera.recipeproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnAdd = findViewById<ImageButton>(R.id.btnAdd)
        val btnView = findViewById<ImageButton>(R.id.btnViewAll)
        val btnFavorites = findViewById<ImageButton>(R.id.btnFavorites)

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
}