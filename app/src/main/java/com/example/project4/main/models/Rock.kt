package com.example.project4.main.models

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking


class Rock(val name: String = "", private var mood: Int = 10, val jokeType: String = "", private var cleaned: Boolean = true){

    fun decreaseMood(){
        mood -= 1
    }

    fun isCleaned(): Boolean{
        return cleaned
    }

}
