package com.example.project4.main.models

import androidx.lifecycle.ViewModel

data class Rock(
    val name: String,
    val mood: Int,
    val jokeType: String
)

class RockViewModel : ViewModel(){

}
