package com.example.pokedex

import java.io.Serializable

data class Pokemon(

    var id: String = "",
    var name: String = "",
    var hp: Int = 0,
    var attack: Int = 0,
    var defense: Int = 0,
    var specialAttack: Int = 0,
    var specialDefense: Int = 0,
    var speed: Int = 0,
    var sprite: String = ""
) : Serializable