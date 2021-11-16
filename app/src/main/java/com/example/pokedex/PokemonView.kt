package com.example.pokedex

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PokemonView(itemView: View): RecyclerView.ViewHolder(itemView) {

    var pokemon: Pokemon? = null
    val rowNameTV: TextView = itemView.findViewById(R.id.rowNameTV)
    val rowImage: ImageView = itemView.findViewById(R.id.rowImage)

}