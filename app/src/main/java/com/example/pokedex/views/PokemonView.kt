package com.example.pokedex.views

import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.PokemonActivity
import com.example.pokedex.R
import com.example.pokedex.model.Pokemon

class PokemonView(itemView: View): RecyclerView.ViewHolder(itemView) {

    var pokemon: Pokemon? = null
    val rowNameTV: TextView = itemView.findViewById(R.id.rowNameTV)
    val rowImage: ImageView = itemView.findViewById(R.id.rowImage)
    val pokemonLayout: ConstraintLayout = itemView.findViewById(R.id.pokemonLayout)

    init {
        pokemonLayout.setOnClickListener {
            val intent = Intent(pokemonLayout.context, PokemonActivity::class.java).apply {
                putExtra("pokemon", pokemon)
            }
            pokemonLayout.context.startActivity(intent)
        }
    }

}