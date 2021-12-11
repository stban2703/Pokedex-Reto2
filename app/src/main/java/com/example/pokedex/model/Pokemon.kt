package com.example.pokedex.model

import java.io.Serializable

data class Pokemon(

    var id: String = "",
    var pokedexNumber: Int = 0,
    var trainerId: String = "",
    var name: String = "",
    var types: ArrayList<String> = ArrayList(),
    var hp: Int = 0,
    var attack: Int = 0,
    var defense: Int = 0,
    var specialAttack: Int = 0,
    var specialDefense: Int = 0,
    var speed: Int = 0,
    var sprite: String = "",
    var date: Long = 0
) : Serializable