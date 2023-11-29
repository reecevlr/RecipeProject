package com.valera.recipeproject

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHandler(context: Context):
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION ) {

    companion object {
        /* Database */
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "RecipeDatabase"

        /* Table */
        const val TABLE_RECIPES = "Recipes"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_INGREDIENTS = "ingredients"
        const val COLUMN_INSTRUCTIONS = "instructions"
        const val COLUMN_FAVORITE = "favorite"
    }

    private val createTableQuery =
        "CREATE TABLE $TABLE_RECIPES (" +
            "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "$COLUMN_NAME TEXT," +
            "$COLUMN_INGREDIENTS TEXT, " +
            "$COLUMN_INSTRUCTIONS TEXT, " +
            "$COLUMN_FAVORITE INTEGER" +
        ")"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_RECIPES")
        onCreate(db)
    }

    /* Functions */
    fun addRecipe(recipe: RecipeModelClass): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put(COLUMN_NAME, recipe.name)
        contentValues.put(COLUMN_INGREDIENTS, recipe.ingredients.joinToString("\n"))
        contentValues.put(COLUMN_INSTRUCTIONS, recipe.instructions)
        contentValues.put(COLUMN_FAVORITE, recipe.favorite)

        val id = db.insert(TABLE_RECIPES, null, contentValues).toInt()
        db.close()

        return id
    }

    @SuppressLint("Range")
    fun viewRecipe(): List<RecipeModelClass> {
        val recipeList: ArrayList<RecipeModelClass> = ArrayList()
        val selectQuery = "SELECT * FROM $TABLE_RECIPES"

        val db = this.readableDatabase
        val cursor: Cursor?

        var recipeId: Int
        var recipeName: String
        var recipeIngredients: List<String>
        var recipeInstructions: String
        var recipeFavorite: Int

        try {
            cursor = db.rawQuery(selectQuery, null)
        }
        catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        if (cursor.moveToFirst()) {
            do {
                recipeId = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
                recipeName = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
                recipeIngredients = cursor.getString(cursor.getColumnIndex(COLUMN_INGREDIENTS)).split("\n")
                recipeInstructions = cursor.getString(cursor.getColumnIndex(COLUMN_INSTRUCTIONS))
                recipeFavorite = cursor.getInt(cursor.getColumnIndex(COLUMN_FAVORITE))

                val recipe = RecipeModelClass(
                    id = recipeId,
                    name = recipeName,
                    ingredients = recipeIngredients,
                    instructions = recipeInstructions,
                    favorite = recipeFavorite == 1
                )
                recipeList.add(recipe)
            }
            while (cursor.moveToNext())
        }
        cursor.close()
        return recipeList
    }

    fun updateFavoriteStatus(recipeId: Int, newFavoriteStatus: Boolean) {
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put(
            COLUMN_FAVORITE,
            if (newFavoriteStatus) {
                1
            }
            else {
                0
            }
        )

        db.update(
            TABLE_RECIPES,
            contentValues,
            "${COLUMN_ID}=?",
            arrayOf(recipeId.toString())
        )
        db.close()
    }

    // TODO: <?> Add update and delete functionalities 
}