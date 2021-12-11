package com.example.pokedex

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.pokedex.databinding.ActivityRegisterBinding
import com.example.pokedex.model.User
import com.example.pokedex.util.CustomToastMessage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val userCollection = Firebase.firestore.collection("users")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registerBtn.setOnClickListener(::register)
    }

    private fun register(view: View) {
        val email = binding.registerEmailET.text.toString()
        val userName = binding.registerNameET.text.toString().lowercase().filter { !it.isWhitespace() }
        val pass = binding.registerPassET.text.toString()
        val confirmPass = binding.checkPassET.text.toString()
        val toast = CustomToastMessage()

        if (email != "" && userName != "" || pass != "" && confirmPass != "") {
            if (pass.length >= 6) {
                if (pass == confirmPass) {
                    toast.createShortTimeToast(this, "Cargando...")
                    Firebase.auth.createUserWithEmailAndPassword(email, pass)
                        .addOnSuccessListener {
                            val id = Firebase.auth.currentUser?.uid
                            val user = User(id!!, email, userName)
                            userCollection.document(id).set(user)
                            val intent = Intent(this, MainActivity::class.java).apply {
                                putExtra("user", user)
                            }
                            startActivity(intent)
                        }
                        .addOnFailureListener {
                            toast.createShortTimeToast(this, it.message!!)
                        }
                } else {
                    toast.createShortTimeToast(this, "Las contraseñas no coinciden")
                }
            } else {
                toast.createShortTimeToast(this, "La contraseña debe tener 6 o más dígitos")
            }
        } else {
            toast.createShortTimeToast(this, "Debes completar todos los campos")
        }
    }
}