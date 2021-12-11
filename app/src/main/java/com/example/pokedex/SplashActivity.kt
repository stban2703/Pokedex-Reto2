package com.example.pokedex

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pokedex.model.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson

class SplashActivity : AppCompatActivity() {

    private var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        user = loadUser()
        if(user == null && Firebase.auth.currentUser == null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            startActivity(Intent(this, PokedexActivity::class.java))
            finish()
        }
    }

    private fun loadUser(): User? {
        val sp = getSharedPreferences("pokedex", MODE_PRIVATE)
        val json = sp.getString("user", "NO_USER")
        return if (json == "NO_USER") {
            null
        } else {
            Gson().fromJson(json, User::class.java)
        }
    }
}