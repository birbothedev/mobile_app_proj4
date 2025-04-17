package com.example.project4.main

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
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
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {
    private lateinit var binding :  ActivityMainBinding
    private lateinit var jokeSetup : String
    private lateinit var jokePunchline : String
    private lateinit var jokeType : String
    private lateinit var rock : Rock
    private var isCleaning by Delegates.notNull<Boolean>()

    private val imageList = listOf(R.drawable.rockhappy, R.drawable.rockdirt1, R.drawable.rockdirt2, R.drawable.rockdirt3,
        R.drawable.rockdirt4, R.drawable.rockdirt5, R.drawable.rockdirt6, R.drawable.maxdirty)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)


        isCleaning = false
        rock = Rock()
        makeRockDirty()

        binding.getJokeButton.setOnClickListener {
            retrieveJoke()
        }

        binding.rockImage.setOnClickListener{
            isCleaning = true
            Log.i("MainActivity", "Is cleaning after click: $isCleaning")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.overflow_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.dark_mode -> {
                item.isChecked = !item.isChecked
                if(item.isChecked){
                    binding.root.setBackgroundColor(Color.parseColor("#034530"))
                } else {
                    binding.root.setBackgroundColor(Color.parseColor("#33ffbd"))
                }

            }
        }
        return true
    }

    private fun makeRockDirty(){
        var rockImageCount = 0
        // run iterations with a 2 second break in between each iteration
        CoroutineScope(Dispatchers.Main).launch{
            if (isCleaning) {
                // Clean rock (go backwards)

                while (rockImageCount > 0) {
                    rockImageCount--
                    binding.rockImage.setImageResource(imageList[rockImageCount])
                    delay(1000)
                }
            } else {
                // Make rock dirty (go forwards)
                while (rockImageCount < imageList.size - 1) {
                    rockImageCount++
                    binding.rockImage.setImageResource(imageList[rockImageCount])
                    delay(1000)
                }
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