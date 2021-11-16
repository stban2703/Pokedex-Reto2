package com.example.pokedex

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class PokemonAdapter : RecyclerView.Adapter<PokemonView>() {

    private val pokemonList = ArrayList<Pokemon>()

    fun addPokemon(pokemon: Pokemon) {
        pokemonList.add(0, pokemon)
        notifyItemInserted(0)
    }

    fun clearList() {
        pokemonList.clear()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonView {
        val inflater = LayoutInflater.from(parent.context)
        val row = inflater.inflate(R.layout.pokemonrow, parent, false)
        val pokemonView = PokemonView(row)
        return pokemonView
    }

    override fun onBindViewHolder(holder: PokemonView, position: Int) {
        val pokemon = pokemonList[position]
        holder.pokemon = pokemon
        holder.rowNameTV.text = pokemon.name
    }

    override fun getItemCount(): Int {
        return pokemonList.size
    }
}