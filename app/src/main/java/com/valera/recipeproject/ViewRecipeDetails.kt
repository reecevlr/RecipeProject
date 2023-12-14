package com.valera.recipeproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class ViewRecipeDetails : AppCompatActivity() {

    private lateinit var tvName: TextView
    private lateinit var tvIngredients: TextView
    private lateinit var tvInstructions: TextView
    private lateinit var imgFavorite: ImageView

    // Track current Recipe ID
    private var recipeId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_recipe_details)

        tvName = findViewById(R.id.tvName)
        tvIngredients = findViewById(R.id.tvIngredients)
        tvInstructions = findViewById(R.id.tvInstructions)
        imgFavorite = findViewById(R.id.imgFavorite)

        val imgEditName = findViewById<ImageView>(R.id.imgEditName)
        val imgEditIng = findViewById<ImageView>(R.id.imgEditIngredients)
        val imgEditInstr = findViewById<ImageView>(R.id.imgEditInstructions)

        val btnReturn = findViewById<Button>(R.id.btnReturnViewRecipes)
        val btnDelete = findViewById<Button>(R.id.btnDelete)

        // Recipe ID from Intent
        recipeId = intent.getIntExtra("recipeId", 0)

        loadRecipeDetails(recipeId)

        imgEditName.setOnClickListener {
            showEditNameDialog()
        }

        imgEditIng.setOnClickListener {
            showEditIngredientsDialog()
        }
        
        imgEditInstr.setOnClickListener {
            showEditInstructionsDialog()
        }

        imgFavorite.setOnClickListener {
            updateFavoriteStatus()
        }

        btnReturn.setOnClickListener {
            startViewRecipes()
        }

        btnDelete.setOnClickListener {
            showDeleteDialog()
        }
    }
    /* Functions */
    private fun startViewRecipes() {
        val origin = intent.getStringExtra("origin")

        if (origin.equals("ViewRecipes")) {
            val i = Intent(this, ViewRecipes :: class.java)
            startActivity(i)
        }
        else {
            val i = Intent(this, FavoriteRecipes :: class.java)
            startActivity(i)
        }
    }

    private fun deleteRecipe(recipeId: Int) {
        val databaseHandler = DatabaseHandler(this)

        databaseHandler.deleteRecipe(recipeId)
        startViewRecipes()
    }

    private fun loadRecipeDetails(recipeId: Int) {
        val databaseHandler = DatabaseHandler(this)
        val recipe = databaseHandler.getRecipeById(recipeId)

        tvName.text = recipe?.name?: ""
        tvIngredients.text = recipe?.ingredients?.joinToString("\n")?: ""
        tvInstructions.text = recipe?.instructions?: ""

        imgFavorite.setImageResource(
            if (recipe?.favorite == true) R.drawable.heart_minus
            else R.drawable.heart_plus
        )
    }

    private fun showDeleteDialog() {
        val dialogBuilder = AlertDialog.Builder(this)

        dialogBuilder.setTitle("Delete Recipe?")
        dialogBuilder.setMessage("This will permanently delete the current recipe.\n\nAre you sure?")

        dialogBuilder.setPositiveButton("Delete") {_, _ ->
            deleteRecipe(recipeId)
        }
        dialogBuilder.setNegativeButton("Cancel") {dialogInterface, _ ->
            dialogInterface.dismiss()
        }

        val deleteDialog = dialogBuilder.create()
        deleteDialog.show()
    }

    private fun showEditIngredientsDialog() {
        val dialogBuilder = AlertDialog.Builder(this, R.style.CustomAlertDialog)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.edit_ingredients, null)

        val etEditIngredients = dialogView.findViewById<EditText>(R.id.etEditIngredients)

        dialogBuilder.setView(dialogView)
        dialogBuilder.setTitle("Edit Ingredients")

        dialogBuilder.setPositiveButton("Save") { _, _ ->
            val newIngredients = etEditIngredients.text.toString()

            val databaseHandler = DatabaseHandler(this)
            databaseHandler.updateIngredients(recipeId, newIngredients)

            loadRecipeDetails(recipeId)
        }

        dialogBuilder.setNegativeButton("Cancel") { dialogInterface, _ ->
            dialogInterface.dismiss()
        }

        val editIngredientsDialog = dialogBuilder.create()
        editIngredientsDialog.show()
    }

    private fun showEditInstructionsDialog() {
        val dialogBuilder = AlertDialog.Builder(this, R.style.CustomAlertDialog)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.edit_instructions, null)

        val etEditInstructions = dialogView.findViewById<EditText>(R.id.etEditInstructions)

        dialogBuilder.setView(dialogView)
        dialogBuilder.setTitle("Edit Instructions")

        dialogBuilder.setPositiveButton("Save") { _, _ ->
            val newInstructions = etEditInstructions.text.toString()

            val databaseHandler = DatabaseHandler(this)
            databaseHandler.updateInstructions(recipeId, newInstructions)

            loadRecipeDetails(recipeId)
        }

        dialogBuilder.setNegativeButton("Cancel") { dialogInterface, _ ->
            dialogInterface.dismiss()
        }

        val editIngredientsDialog = dialogBuilder.create()
        editIngredientsDialog.show()
    }

    private fun showEditNameDialog() {
        val dialogBuilder = AlertDialog.Builder(this, R.style.CustomAlertDialog)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.edit_name, null)

        val etEditName = dialogView.findViewById<EditText>(R.id.etEditName)

        dialogBuilder.setView(dialogView)
        dialogBuilder.setTitle("Edit Recipe Name")

        dialogBuilder.setPositiveButton("Save") { _, _ ->
            val newName = etEditName.text.toString()

            if (newName != "") {
                val databaseHandler = DatabaseHandler(this)
                databaseHandler.updateName(recipeId, newName)

                loadRecipeDetails(recipeId)
            }
            else {
                Toast.makeText(this, "Error: Check input name!", Toast.LENGTH_SHORT).show()
            }
        }

        dialogBuilder.setNegativeButton("Cancel") { dialogInterface, _ ->
            dialogInterface.dismiss()
        }

        val editIngredientsDialog = dialogBuilder.create()
        editIngredientsDialog.show()
    }

    private fun updateFavoriteStatus() {
        val databaseHandler = DatabaseHandler(this)
        val newFavoriteStatus = !databaseHandler.getRecipeById(recipeId)?.favorite!!

        databaseHandler.updateFavoriteStatus(recipeId, newFavoriteStatus)

        loadRecipeDetails(recipeId)
    }
}