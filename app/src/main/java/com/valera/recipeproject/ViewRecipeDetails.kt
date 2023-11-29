package com.valera.recipeproject

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class ViewRecipeDetails : AppCompatActivity() {

    private lateinit var tvName: TextView
    private lateinit var tvIngredients: TextView
    private lateinit var tvInstructions: TextView

    // Track current Recipe ID
    private var recipeId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_recipe_details)

        tvName = findViewById(R.id.tvName)
        tvIngredients = findViewById(R.id.tvIngredients)
        tvInstructions = findViewById(R.id.tvInstructions)

        val imgEditIng = findViewById<ImageView>(R.id.imgEditIngredients)
        val imgEditInstr = findViewById<ImageView>(R.id.imgEditInstructions)

        val btnReturn = findViewById<Button>(R.id.btnReturnViewRecipes)
        val btnDelete = findViewById<Button>(R.id.btnDelete)

        // Recipe ID from Intent
        recipeId = intent.getIntExtra("recipeId", 0)

        loadRecipeDetails(recipeId)
        
        imgEditIng.setOnClickListener {
            showEditIngredientsDialog()
        }
        
        imgEditInstr.setOnClickListener {
            showEditInstructionsDialog()
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
        val i = Intent(this, ViewRecipes :: class.java)
        startActivity(i)
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
        val dialogBuilder = AlertDialog.Builder(this)
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

    }
}