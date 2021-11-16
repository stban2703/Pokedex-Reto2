package com.example.pokedex

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.pokedex.databinding.ActivityPokemonBinding

class PokemonActivity : AppCompatActivity() {

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
        binding.hpTV.text = pokemon.hp.toString()
        binding.attackTV.text = pokemon.attack.toString()
        binding.defenseTV.text = pokemon.defense.toString()
        binding.spAttackTV.text = pokemon.specialAttack.toString()
        binding.spDefenseTV.text = pokemon.specialDefense.toString()
        binding.speedTV.text = pokemon.speed.toString()
    }
}