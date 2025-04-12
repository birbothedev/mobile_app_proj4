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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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

        rock = Rock()


        binding.getJokeButton.setOnClickListener {
            Log.d("MainActivity", "Button clicked")
            retrieveJoke()
        }
        makeRockDirty()
    }

    private fun makeRockDirty(){
        val imageList = listOf(R.drawable.rockhappy, R.drawable.rockdirt1, R.drawable.rockdirt2, R.drawable.rockdirt3,
            R.drawable.rockdirt4, R.drawable.rockdirt5, R.drawable.rockdirt6, R.drawable.maxdirty)
        // run iterations with a 2 second break in between each iteration
        CoroutineScope(Dispatchers.Main).launch{
            for (i in imageList.indices) {
                binding.rockImage.setImageResource(imageList[i])
                delay(1000)
            }
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