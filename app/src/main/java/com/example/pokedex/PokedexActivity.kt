package com.example.pokedex

import android.os.Bundle
import android.os.Looper
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
import java.lang.Exception

class PokedexActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPokedexBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPokedexBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.catchBtn.setOnClickListener {
            getPokemon()
        }
    }

    private fun getPokemon() {
        //Log.e("Pokemon>>>", "Ok")
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val json =
                    HTTPSWebUtilDomi().GETRequest("https://pokeapi.co/api/v2/pokemon/ditto")
                //val json = Gson().toJson(response)
                val gson = Gson()
                val response = gson.fromJson(json, Response::class.java)
                Log.e("Pokemon>>>", response.name)
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(applicationContext, "Pokemon no encontrado", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            //val json = Gson().toJson(response)
            //withContext(Dispatchers.Main) {}
            //Log.e("Pokemon>>>", json)
            //Toast.makeText(applicationContext, "Pokemon", Toast.LENGTH_SHORT).show()
        }
    }
}