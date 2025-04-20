package com.example.project4.main

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
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
import org.json.JSONArray
import org.json.JSONObject
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity(), RockStats.RockStatsListener {
    private lateinit var binding :  ActivityMainBinding
    private lateinit var jokeSetup : String
    private lateinit var jokePunchline : String
    private lateinit var jokeType : String
    private lateinit var rock : Rock
    private var isCleaning by Delegates.notNull<Boolean>()
    private val desiredJokeType = mutableListOf<String>()
    private var rockImageCount = 0
    private var lastCleanTime : Long = 0

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
            if(rock.isCleaned()){
                retrieveJoke()
                rock.addJokeCount()
            }
            else {
                binding.setupText.text = "Your rock is too unhappy to talk to you right now!"
                binding.punchlineText.text = " "
            }
        }

        binding.rockImage.setOnClickListener{
            if (!isCleaning){
                isCleaning = true
                cleanRock()
                lastCleanTime = System.currentTimeMillis()
                rock.addCleanCount()
            }
        }

        binding.statsButton.setOnClickListener {
            // In your Activity or Fragment
            val bottomSheetDialog = RockStats()

            bottomSheetDialog.setJokeStats(rock.getJokeCount())
            bottomSheetDialog.setCleanCount(rock.getCleanedCount())

            bottomSheetDialog.listener = this
            bottomSheetDialog.show(supportFragmentManager, "RockStats")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.overflow_menu, menu)

        menu.findItem(R.id.general).isChecked = "general" in desiredJokeType
        menu.findItem(R.id.programming).isChecked = "programming" in desiredJokeType
        menu.findItem(R.id.knockknock).isChecked = "knock-knock" in desiredJokeType
        menu.findItem(R.id.dad).isChecked = "dad" in desiredJokeType


        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.dark_mode -> {
                item.isChecked = !item.isChecked
                if(item.isChecked){
                    binding.root.setBackgroundColor(Color.parseColor("#034530"))
                    binding.rockName.setTextColor(Color.parseColor("#ffffff"))
                    binding.setupText.setTextColor(Color.parseColor("#ffffff"))
                    binding.punchlineText.setTextColor(Color.parseColor("#ffffff"))
                } else {
                    binding.root.setBackgroundColor(Color.parseColor("#33ffbd"))
                }
            }
            R.id.general -> {
                item.isChecked = !item.isChecked
                if (item.isChecked) {
                    desiredJokeType.add("general")
                } else {
                    desiredJokeType.remove("general")
                }
            }
            R.id.programming -> {
                item.isChecked = !item.isChecked
                if (item.isChecked) {
                    desiredJokeType.add("programming")
                } else {
                    desiredJokeType.remove("programming")
                }
            }
            R.id.dad -> {
                item.isChecked = !item.isChecked
                if (item.isChecked) {
                    desiredJokeType.add("dad")
                } else {
                    desiredJokeType.remove("dad")
                }
            }
            R.id.knockknock -> {
                item.isChecked = !item.isChecked
                if (item.isChecked) {
                    desiredJokeType.add("knock-knock")
                } else {
                    desiredJokeType.remove("knock-knock")
                }
            }
        }
        return true
    }

    private fun makeRockDirty(){
        // run iterations with a 2 second break in between each iteration
        CoroutineScope(Dispatchers.Main).launch{
            while (true) {
                delay(3000)
                val currentTime = System.currentTimeMillis()
                val timeSinceClean = currentTime - lastCleanTime
                // Make rock dirty (go forwards)
                if (rockImageCount < imageList.size - 1 && timeSinceClean > 5000) {
                    rockImageCount++
                    binding.rockImage.setImageResource(imageList[rockImageCount])
                }
                if (rockImageCount >= 7){
                    rock.setCleaned(false)
                }
            }
        }
    }

    private fun cleanRock(){
        // Clean rock (go backwards)
        if (rockImageCount > 0) {
            rockImageCount--
            binding.rockImage.setImageResource(imageList[rockImageCount])
        }
        if (rockImageCount <= 7){
            rock.setCleaned(true)
        }
        isCleaning = false
    }

    private fun retrieveJoke() {
        val jokeURL = "https://official-joke-api.appspot.com/jokes/random/250"
        val queue = Volley.newRequestQueue(this)

        val stringRequest = StringRequest (
            Request.Method.GET, jokeURL,
            {response ->
                val jokeArray = JSONArray(response)
                val filteredJokes = mutableListOf<JSONObject>()

                for (i in 0 until jokeArray.length()) {
                    val jokeObject = jokeArray.getJSONObject(i)
                    jokeType = jokeObject.getString("type")

                    if (desiredJokeType.contains(jokeType)){
                        filteredJokes.add(jokeObject)
                    }

                    if (filteredJokes.isNotEmpty()){
                        val joke = filteredJokes.random()

                        jokeSetup = joke.getString("setup")
                        jokePunchline = joke.getString("punchline")

                        binding.setupText.text = jokeSetup
                        binding.punchlineText.text = jokePunchline
                    } else {
                        binding.setupText.text = "No jokes of this type."
                        binding.punchlineText.text = " "
                    }

                }
            },
            {
                Log.i("MainActivity", "That didn't work!")
        })

        queue.add(stringRequest)
    }

    override fun onRockNameEntered(name: String) {
        rock.setName(name)
        binding.rockName.text = rock.getName()
    }
}