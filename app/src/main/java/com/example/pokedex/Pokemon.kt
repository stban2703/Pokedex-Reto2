package com.example.pokedex

import java.io.Serializable

data class Pokemon(

    var id: String = "",
    var name: String = "",
    var attack: Number = 0,
    var defense: Number = 0,
    var specialAttack: Number = 0,
    var specialDefense: Number = 0,
    var speed: Number = 0,
    var sprite: String = ""
) : Serializable