package com.example.pokedex

import android.content.Intent
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
            if (pokemonName != "") {
                catchPokemon(pokemonName)
            } else {
                Toast.makeText(
                    this,
                    "Escribe el nombre del Pokemon que quieres capturar",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.searchBtn.setOnClickListener {
            val pokemonName =
                binding.searchPokemonET.text.toString().lowercase().filter { !it.isWhitespace() }
            if (pokemonName != "") {
                searchPokemon(pokemonName)
            } else {
                Toast.makeText(this, "Escribe el nombre del Pokemon que buscas", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun getUserPokemon() {
        pokedexCollection.document(user.id).collection("pokedex")
            .orderBy("date").addSnapshotListener { value, error ->
                for (change in value!!.documentChanges) {
                    Log.e(">>>", value.documents.size.toString())
                    when (change.type) {
                        DocumentChange.Type.ADDED -> {
                            val pokemon = change.document.toObject(Pokemon::class.java)
                            adapter?.addPokemon(pokemon)
                            layoutManager.scrollToPosition(0)
                        }

                        DocumentChange.Type.REMOVED -> {
                            val deletedPokemon = change.document.toObject(Pokemon::class.java)
                            //Log.e(">>>", deletedPokemon.name)
                            adapter?.removePokemon(deletedPokemon)
                        }
                    }
                }
            }
    }

    private fun searchPokemon(name: String) {
        pokedexCollection.document(user.id).collection("pokedex")
            .whereEqualTo("name", name).get()
            .addOnCompleteListener {
                if (it.result?.size() == 0) {
                    Toast.makeText(this, "No has capturado a este Pokemon", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    lateinit var existingPokemon: Pokemon
                    for (document in it.result!!) {
                        existingPokemon = document.toObject(Pokemon::class.java)
                        break
                    }
                    val intent = Intent(this, PokemonActivity::class.java).apply {
                        putExtra("pokemon", existingPokemon)
                    }
                    startActivity(intent)
                }
            }
    }

    private fun catchPokemon(pokemonName: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val json =
                    HTTPSWebUtilDomi().GETRequest("https://pokeapi.co/api/v2/pokemon/${pokemonName}")
                val gson = Gson()
                val response = gson.fromJson(json, Response::class.java)

                val caughtPokemon = Pokemon()

                caughtPokemon.pokedexNumber = response.id
                caughtPokemon.name = response.name
                caughtPokemon.hp = response.stats[0].base_stat
                caughtPokemon.attack = response.stats[1].base_stat
                caughtPokemon.defense = response.stats[2].base_stat
                caughtPokemon.specialAttack = response.stats[3].base_stat
                caughtPokemon.specialDefense = response.stats[4].base_stat
                caughtPokemon.speed = response.stats[5].base_stat
                caughtPokemon.sprite = response.sprites.front_default
                caughtPokemon.date = Calendar.getInstance().time.time
                for (type in response.types) {
                    caughtPokemon.types.add(type.type.name)
                }
                caughtPokemon.trainerId = user.id

                val newPokemonRef = pokedexCollection.document(user.id)
                    .collection("pokedex").document()

                caughtPokemon.id = newPokemonRef.id
                newPokemonRef.set(caughtPokemon)

                withContext(Dispatchers.Main) {
                    Toast.makeText(applicationContext, "Pokemon atrapado", Toast.LENGTH_SHORT)
                        .show()
                }

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