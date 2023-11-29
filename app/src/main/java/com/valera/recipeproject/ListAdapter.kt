package com.valera.recipeproject

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class ListAdapter(
        private val context: Activity,
        private val recipeName: Array<String>,
        private val recipeFavorite: Array<Boolean>,
        private val onFavoriteClickListener: (Int) -> Unit
    )
    : ArrayAdapter<String>(context, R.layout.custom_list, recipeName)
{
    @SuppressLint("ViewHolder", "InflateParams")    // Remove if encountering issues
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.custom_list, null, true)

        val recipeNameTxt = rowView.findViewById<TextView>(R.id.listRecipeName)
        val recipeFavoriteImg = rowView.findViewById<ImageView>(R.id.listImage)

        recipeNameTxt.text = recipeName[position]
        recipeFavoriteImg.setImageResource(
            if (recipeFavorite[position]) R.drawable.heart_minus
            else R.drawable.heart_plus
        )

        // Click listener for ImageView
        recipeFavoriteImg.setOnClickListener {
            onFavoriteClickListener.invoke(position)
        }

        return rowView
    }
}