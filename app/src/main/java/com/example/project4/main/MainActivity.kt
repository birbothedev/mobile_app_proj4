package com.example.project4.main

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.project4.databinding.ActivityMainBinding
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private lateinit var binding :  ActivityMainBinding
    private lateinit var jokeSetup : String
    private lateinit var jokePunchline : String
    private lateinit var jokeType : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.getJokeButton.setOnClickListener {
            Log.d("MainActivity", "Button clicked")
            retrieveJoke()
        }
    }

    private fun retrieveJoke() {
        val jokeURL = "https://official-joke-api.appspot.com/random_joke"
        val queue = Volley.newRequestQueue(this)

        val stringRequest = StringRequest (
            Request.Method.GET, jokeURL,
            {response ->
                val jokeObject = JSONObject(response)

                jokeType = jokeObject.getString("type")
                jokeSetup = jokeObject.getString("setup")
                jokePunchline = jokeObject.getString("punchline")


                binding.setupText.text = jokeSetup
                binding.punchlineText.text = jokePunchline
            },
            {
                Log.i("MainActivity", "That didn't work!")
        })

        queue.add(stringRequest)
    }
}