package com.example.pokedex

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.pokedex.databinding.ActivityMainBinding
import com.example.pokedex.model.User
import com.example.pokedex.util.CustomToastMessage
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.Login
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {

    private val userCollection = Firebase.firestore.collection("users")
    private lateinit var binding: ActivityMainBinding
    private val callbackManager = CallbackManager.Factory.create();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.registerHereBtn.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.loginBtn.setOnClickListener(::login)

        binding.facebookBtn.setOnClickListener {
            LoginManager.getInstance().logInWithReadPermissions(this, listOf("email"))
            LoginManager.getInstance().registerCallback(
                callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(result: LoginResult) {
                        result.let {
                            val token = it.accessToken
                            val credential = FacebookAuthProvider.getCredential(token.token)
                            FirebaseAuth.getInstance().signInWithCredential(credential)
                                .addOnCompleteListener { task ->
                                    if(task.isSuccessful) {
                                        val id = task.result!!.user!!.uid
                                        val userEmail = task.result?.user?.email
                                        val userName = task.result?.user?.displayName
                                        val user = User(id, userEmail!!, userName!!)
                                        userCollection.document(id).set(user)
                                        saveUser(user)
                                        val intent = Intent(applicationContext, PokedexActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }
                                }
                        }
                    }

                    override fun onCancel() {

                    }

                    override fun onError(error: FacebookException) {
                        Log.e(">>>", error.message!!)
                    }
                }
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
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
                    finish()
                }
            }.addOnFailureListener {
                toast.createShortTimeToast(this, it.message!!)
            }
        } else {
            toast.createShortTimeToast(this, "Debes completar todos los campos")
        }
    }

    private fun navigateToPokedex() {

    }

    private fun saveUser(user: User) {
        val sp = getSharedPreferences("pokedex", MODE_PRIVATE)
        val json = Gson().toJson(user)
        sp.edit().putString("user", json).apply()
    }
}