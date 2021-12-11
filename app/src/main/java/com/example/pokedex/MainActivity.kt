package com.example.pokedex

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.pokedex.databinding.ActivityMainBinding
import com.example.pokedex.model.User
import com.example.pokedex.util.CustomToastMessage
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {

    private val userCollection = Firebase.firestore.collection("users")
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.registerHereBtn.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.loginBtn.setOnClickListener(::login)
    }

    private fun login(view: View) {
        val email = binding.userEmailET.text.toString()
        val pass = binding.passET.text.toString()
        val toast = CustomToastMessage()

        if (email != "") {
            toast.createShortTimeToast(this, "Cargando...")
            Firebase.auth.signInWithEmailAndPassword(email, pass).addOnSuccessListener {
                val fbUser = Firebase.auth.currentUser
                userCollection.document(fbUser!!.uid).get().addOnSuccessListener {
                    val user = it.toObject(User::class.java)
                    saveUser(user!!)
                    val intent = Intent(this, PokedexActivity::class.java)
                    startActivity(intent)
                }
            }.addOnFailureListener {
                toast.createShortTimeToast(this, it.message!!)
            }
        } else {
            toast.createShortTimeToast(this, "Debes completar todos los campos")
        }
    }

    private fun saveUser(user: User) {
        val sp = getSharedPreferences("pokedex", MODE_PRIVATE)
        val json = Gson().toJson(user)
        sp.edit().putString("user", json).apply()
    }
}