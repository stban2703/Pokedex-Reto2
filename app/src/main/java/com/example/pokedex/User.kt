package com.example.pokedex

import java.io.Serializable

data class User(
    var id: String = "",
    var username: String = "",
): Serializable