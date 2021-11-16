package com.example.pokedex

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pokedex.databinding.ActivityPokedexBinding
import com.example.pokedex.pokedex.Response
import com.example.pokedex.util.HTTPSWebUtilDomi
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class PokedexActivity : AppCompatActivity() {

    private val pokedexCollection = Firebase.firestore.collection("pokedex")
    private lateinit var binding: ActivityPokedexBinding
    private lateinit var layoutManager: LinearLayoutManager
    var adapter: PokemonAdapter? = null
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPokedexBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Recuperar informacion del usuario
        user = intent.extras?.getSerializable("user") as User

        // Mostrar pokemon atrapados
        adapter = PokemonAdapter()
        layoutManager = LinearLayoutManager(this)
        binding.recycler.layoutManager = layoutManager
        binding.recycler.setHasFixedSize(true)
        binding.recycler.adapter = adapter
        adapter?.clearList()
        getUserPokemon()

        binding.catchBtn.setOnClickListener {
            val pokemonName =
                binding.catchPokemonET.text.toString().lowercase().filter { !it.isWhitespace() }
            catchPokemon(pokemonName)
        }
    }

    private fun getUserPokemon() {
        pokedexCollection.document(user.id).collection("pokedex")
            .orderBy("date").addSnapshotListener { value, error ->
                if (value?.documents?.size == 0) {
                    Toast
                        .makeText(this, "No tienes ningún Pokemon por ahora", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    for(change in value!!.documentChanges) {
                        when(change.type){
                            DocumentChange.Type.ADDED -> {
                                val pokemon = change.document.toObject(Pokemon::class.java)
                                adapter?.addPokemon(pokemon)
                            }
                        }
                    }
                }
            }
    }

    private fun catchPokemon(pokemonName: String) {
        //Log.e("Pokemon>>>", "Ok")
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val json =
                    HTTPSWebUtilDomi().GETRequest("https://pokeapi.co/api/v2/pokemon/${pokemonName}")
                val gson = Gson()
                val response = gson.fromJson(json, Response::class.java)

                val caughtPokemon = Pokemon()
                caughtPokemon.id = response.id
                caughtPokemon.name = response.name
                caughtPokemon.hp = response.stats[0].base_stat
                caughtPokemon.attack = response.stats[1].base_stat
                caughtPokemon.defense = response.stats[2].base_stat
                caughtPokemon.specialAttack = response.stats[3].base_stat
                caughtPokemon.specialDefense = response.stats[4].base_stat
                caughtPokemon.speed = response.stats[5].base_stat
                caughtPokemon.sprite = response.sprites.front_default
                caughtPokemon.date = Calendar.getInstance().time.time

                pokedexCollection.document(user.id).collection("pokedex")
                    .document(caughtPokemon.name).set(caughtPokemon)

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