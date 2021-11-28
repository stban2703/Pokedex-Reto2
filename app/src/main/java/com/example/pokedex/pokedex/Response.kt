package com.example.pokedex.pokedex

data class Response(
    var id: Int = 0,
    var name: String = "",
    var sprites: Sprite = Sprite(),
    var stats: ArrayList<StatResult> = ArrayList(),
    var types: ArrayList<TypeResult> = ArrayList()
)