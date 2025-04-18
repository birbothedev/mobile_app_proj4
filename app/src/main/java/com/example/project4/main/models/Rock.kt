package com.example.project4.main.models

class Rock {
    private var name : String = ""
    private var cleaned: Boolean = true

    fun setName(name : String){
        this.name = name
    }

    fun setCleaned(cleaned : Boolean){
        this.cleaned = cleaned
    }

    fun isCleaned(): Boolean {
        return cleaned
    }

}
