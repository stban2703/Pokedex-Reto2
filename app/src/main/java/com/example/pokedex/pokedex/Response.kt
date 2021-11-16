package com.example.pokedex.pokedex

data class Response(
    var id: String = "",
    var name: String = "",
    var sprites: Sprite = Sprite(),
    var stats: ArrayList<StatResult> = ArrayList(),
    var types: ArrayList<TypeResult> = ArrayList()
)