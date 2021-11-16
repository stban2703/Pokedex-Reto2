package com.example.pokedex.pokedex

data class Response(
    var id: String = "",
    var name: String = "",
    var stats: ArrayList<StatResult> = ArrayList(),
    var types: ArrayList<TypeResult> = ArrayList()
)