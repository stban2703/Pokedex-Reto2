package com.example.pokedex

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pokedex.util.BitmapFromUrl
import java.io.File
import java.io.FileOutputStream
import java.net.URL

class PokemonAdapter : RecyclerView.Adapter<PokemonView>() {

    private val pokemonList = ArrayList<Pokemon>()

    fun addPokemon(pokemon: Pokemon) {
        pokemonList.add(0, pokemon)
        notifyItemInserted(0)
    }

    fun removePokemon(pokemon: Pokemon) {
        val index = pokemonList.indexOf(pokemon)
        pokemonList.removeAt(index)
        notifyItemRemoved(index)
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
        holder.rowNameTV.text = pokemon.name.replaceFirstChar {
            it.uppercase()
        }
        Glide.with(holder.itemView.context).load(pokemon.sprite).centerCrop().into(holder.rowImage)
    }

    override fun getItemCount(): Int {
        return pokemonList.size
    }

    fun download(link: String, path: String) {
        URL(link).openStream().use { input ->
            FileOutputStream(File(path)).use { output ->
                input.copyTo(output)
            }
        }
    }
}