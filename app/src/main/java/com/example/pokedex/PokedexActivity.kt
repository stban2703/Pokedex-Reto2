package com.example.pokedex

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.pokedex.databinding.ActivityPokedexBinding
import com.example.pokedex.pokedex.Response
import com.example.pokedex.util.HTTPSWebUtilDomi
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PokedexActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPokedexBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPokedexBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.catchBtn.setOnClickListener {
            val pokemonName = binding.catchPokemonET.text.toString().lowercase().filter { !it.isWhitespace() }
            getPokemon(pokemonName)
        }
    }

    private fun getPokemon(pokemonName: String) {
        //Log.e("Pokemon>>>", "Ok")
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val json =
                    HTTPSWebUtilDomi().GETRequest("https://pokeapi.co/api/v2/pokemon/${pokemonName}")
                val gson = Gson()
                val response = gson.fromJson(json, Response::class.java)

                Log.e("Pokemon>>>", response.name)

                Log.e("Pokemon>>>", response.stats[0].stat.name)
                Log.e("Pokemon>>>", response.stats[0].base_stat.toString())

                Log.e("Pokemon>>>", response.stats[1].stat.name)
                Log.e("Pokemon>>>", response.stats[1].base_stat.toString())

                Log.e("Pokemon>>>", response.stats[2].stat.name)
                Log.e("Pokemon>>>", response.stats[2].base_stat.toString())

                Log.e("Pokemon>>>", response.stats[3].stat.name)
                Log.e("Pokemon>>>", response.stats[3].base_stat.toString())

                Log.e("Pokemon>>>", response.stats[4].stat.name)
                Log.e("Pokemon>>>", response.stats[4].base_stat.toString())

                Log.e("Pokemon>>>", response.stats[5].stat.name)
                Log.e("Pokemon>>>", response.stats[5].base_stat.toString())

            } catch (e: Exception) {
                // Si no encuentra un pokemon o el nombre es incorrecto
                withContext(Dispatchers.Main) {
                    Toast.makeText(applicationContext, "Pokemon no encontrado", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
}