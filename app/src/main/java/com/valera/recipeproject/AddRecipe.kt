package com.valera.recipeproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddRecipe : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_recipe)

        val btnAddRecipe = findViewById<Button>(R.id.btnAddRecipe)
        val btnReturn = findViewById<Button>(R.id.btnReturn)

        btnAddRecipe.setOnClickListener {
            addRecipe()
        }
        btnReturn.setOnClickListener {
            startMainActivity()
        }
    }

    /* Functions */
    private fun startMainActivity() {
        val i = Intent(this, MainActivity :: class.java)
        startActivity(i)
    }

    private fun addRecipe() {
        val recipeName = findViewById<EditText>(R.id.etRecipeName)
        val recipeIngredients = findViewById<EditText>(R.id.etRecipeIngredients)
        val recipeInstructions = findViewById<EditText>(R.id.etRecipeInstructions)

        val name = recipeName.text.toString()
        val ingredients = recipeName.text.toString()
        val instructions = recipeName.text.toString()

        val databaseHandler = DatabaseHandler(this)

        if (isValid(name, ingredients, instructions)) {
            val status =
                databaseHandler
                    .addRecipe(RecipeModelClass(null, name, ingredients.lines(), instructions))

            if (status > -1) {
                Toast
                    .makeText(this, "Recipe saved!", Toast.LENGTH_SHORT)
                    .show()

                recipeName.text.clear()
                recipeIngredients.text.clear()
                recipeInstructions.text.clear()
            }
        }
        else {
            Toast
                .makeText(this, "No fields can be left blank!", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun isValid(
        recipeName: String,
        recipeIngredients: String,
        recipeInstructions: String
    ): Boolean {
        // Verify emptiness
        if (
            recipeName.trim() == "" ||
            recipeIngredients.trim() == "" ||
            recipeInstructions.trim() == ""
        ) {
            return false
        }
        return true
    }
}