package com.valera.recipeproject

class RecipeModelClass(
    val name: String,
    val ingredients: List<String>,
    val instructions: String,
    var favorite: Boolean = false
)