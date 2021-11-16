package com.example.pokedex

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.pokedex.databinding.ActivityPokedexBinding
import com.example.pokedex.pokedex.Response
import com.example.pokedex.util.HTTPSWebUtilDomi
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PokedexActivity : AppCompatActivity() {

    private val pokedexCollection = Firebase.firestore.collection("pokedex")
    private lateinit var binding: ActivityPokedexBinding
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPokedexBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Recuperar informacion del usuario
        user = intent.extras?.getSerializable("user") as User

        binding.catchBtn.setOnClickListener {
            val pokemonName =
                binding.catchPokemonET.text.toString().lowercase().filter { !it.isWhitespace() }
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

                val catchPokemon = Pokemon()
                catchPokemon.id = response.id
                catchPokemon.name = response.name
                catchPokemon.hp = response.stats[0].base_stat
                catchPokemon.attack = response.stats[1].base_stat
                catchPokemon.defense = response.stats[2].base_stat
                catchPokemon.specialAttack = response.stats[3].base_stat
                catchPokemon.specialDefense = response.stats[4].base_stat
                catchPokemon.speed = response.stats[5].base_stat
                catchPokemon.sprite = response.sprites.front_default

                pokedexCollection.document(user.id).collection("pokedex")
                    .document(catchPokemon.name).set(catchPokemon)

                withContext(Dispatchers.Main) {
                    Toast.makeText(applicationContext, "Pokemon atrapado", Toast.LENGTH_SHORT)
                        .show()
                }

            } catch (e: Exception) {
                Log.e(">>>", e.toString())
                // Si no encuentra un pokemon o el nombre es incorrecto
                withContext(Dispatchers.Main) {
                    Toast.makeText(applicationContext, "Pokemon no encontrado", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
}