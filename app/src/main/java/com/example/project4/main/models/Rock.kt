package com.example.project4.main.models

class Rock {
    private var name : String = ""
    private var cleaned: Boolean = true
    private var jokeCount : Int = 0
    private var cleanedCount : Int = 0

    fun getName(): String {
        return name
    }

    fun setName(name : String){
        this.name = name
    }

    fun setCleaned(cleaned : Boolean){
        this.cleaned = cleaned
    }

    fun isCleaned(): Boolean {
        return cleaned
    }

    fun addCleanCount(){
        cleanedCount ++
    }

    fun getCleanedCount(): Int{
        return cleanedCount
    }

    fun addJokeCount(){
        cleanedCount ++
    }

    fun getJokeCount(): Int{
        return cleanedCount
    }

}
