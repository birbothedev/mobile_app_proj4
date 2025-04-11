package com.example.project4.main

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.project4.R
import com.example.project4.databinding.ActivityMainBinding
import com.example.project4.main.models.Rock
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private lateinit var binding :  ActivityMainBinding
    private lateinit var jokeSetup : String
    private lateinit var jokePunchline : String
    private lateinit var jokeType : String
    private lateinit var rock : Rock

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.getJokeButton.setOnClickListener {
            Log.d("MainActivity", "Button clicked")
            retrieveJoke()
        }

        makeRockDirty()
    }

    private fun makeRockDirty(){
        var rockImageCount = 0
        val imageList = listOf(R.drawable.rockhappy)
        // run iterations with a 2 second break in between each iteration
        runBlocking {
            val interval: Long = 2000
            val iterations = 10

            repeat(iterations){
                rock.decreaseMood()
                rockImageCount += 1
                delay(interval)
            }
        }
        binding.rockImage.setImageResource(imageList[rockImageCount])
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