package com.example.pokedex.model

import java.io.Serializable

data class User(
    var id: String = "",
    var email: String = "",
    var username: String = "",
): Serializable