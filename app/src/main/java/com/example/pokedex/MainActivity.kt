package com.example.pokedex

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pokedex.databinding.ActivityMainBinding
import com.example.pokedex.model.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

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
            val user = User("", username)
            val query = userCollection.whereEqualTo("username", user.username)

            if (username != "") {
                Toast.makeText(this, "Cargando...", Toast.LENGTH_SHORT).show()
                query.get().addOnCompleteListener {
                    if (it.result?.size() == 0) {
                        // Crea el usuario si no existe
                        val newUserRef = userCollection.document()
                        user.id = newUserRef.id
                        newUserRef.set(user)
                        login(user)

                    } else {

                        // Si existe, trae la informacion de ese usuario
                        lateinit var existingUser: User
                        for (document in it.result!!) {
                            existingUser = document.toObject(User::class.java)
                            break
                        }
                        login(existingUser)
                    }
                }
            } else {
                Toast.makeText(this, "Debes ingresar tu nombre de usuario", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun login(user: User) {
        val intent = Intent(this, PokedexActivity::class.java).apply {
            putExtra("user", user)
        }
        startActivity(intent)
    }
}