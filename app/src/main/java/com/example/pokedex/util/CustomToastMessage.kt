package com.example.pokedex.util

import android.content.Context
import android.widget.Toast

class CustomToastMessage {
    fun createShortTimeToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}