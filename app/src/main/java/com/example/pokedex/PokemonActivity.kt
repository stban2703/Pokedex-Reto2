package com.example.pokedex

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.pokedex.databinding.ActivityPokemonBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PokemonActivity : AppCompatActivity() {

    private val pokedexCollection = Firebase.firestore.collection("pokedex")
    private lateinit var pokemon: Pokemon
    private lateinit var binding: ActivityPokemonBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPokemonBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        pokemon = intent.extras?.getSerializable("pokemon") as Pokemon
        Glide.with(this).load(pokemon.sprite).centerCrop().into(binding.pokemonImage)
        binding.pokemonNameTV.text = pokemon.name.replaceFirstChar {
            it.uppercase()
        }
        var pokemonType = ""
        for(type in pokemon.types) {
            val newType = type.replaceFirstChar {it.uppercase()}
            pokemonType += "$newType/"
        }
        binding.pokemonTypeTV.text = pokemonType.substring(0, pokemonType.length - 1)
        binding.hpTV.text = pokemon.hp.toString()
        binding.attackTV.text = pokemon.attack.toString()
        binding.defenseTV.text = pokemon.defense.toString()
        binding.spAttackTV.text = pokemon.specialAttack.toString()
        binding.spDefenseTV.text = pokemon.specialDefense.toString()
        binding.speedTV.text = pokemon.speed.toString()

        binding.releaseBtn.setOnClickListener {
            Toast.makeText(this, "Liberando...", Toast.LENGTH_SHORT).show()
            pokedexCollection.document(pokemon.trainerId)
                .collection("pokedex").document(pokemon.id).delete()
                .addOnSuccessListener { finish() }
                .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
        }
    }
}