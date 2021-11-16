package com.example.pokedex

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.pokedex.databinding.ActivityMainBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class MainActivity : AppCompatActivity() {

    private val userCollection = Firebase.firestore.collection("users")
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.loginBtn.setOnClickListener {
            val username =
                binding.usernameET.text.toString().lowercase().filter { !it.isWhitespace() }
            val user = User(UUID.randomUUID().toString(), username)
            val query = userCollection.whereEqualTo("username", user.username)

            if (username != "") {
                query.get().addOnCompleteListener {
                    if (it.result?.size() == 0) {
                        // Crea el usuario si no existe
                        userCollection.document(user.id).set(user)
                        val intent = Intent(this, PokedexActivity::class.java).apply {
                            putExtra("user", user)
                        }
                        startActivity(intent)

                    } else {
                        // Si existe, trae la informacion de ese usuario
                        lateinit var existingUser: User
                        for (document in it.result!!) {
                            existingUser = document.toObject(User::class.java)
                            break
                        }
                        val intent = Intent(this, PokedexActivity::class.java).apply {
                            putExtra("user", existingUser)
                        }
                        startActivity(intent)
                    }
                }
            } else {
                Toast.makeText(this, "Debes ingresar tu nombre de usuario", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}