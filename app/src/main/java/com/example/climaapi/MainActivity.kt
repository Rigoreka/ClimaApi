package com.example.climaapi

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.climaapi.databinding.ActivityMainBinding
import com.github.kittinunf.fuel.Fuel
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnBusqueda.setOnClickListener {
            val cityName = binding.txtNota.text.toString().trim()
            if (cityName.isNotEmpty()) {
                fetchWeatherData(cityName)
            } else {
                Toast.makeText(this, "Please enter a city name", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchWeatherData(cityName: String) {
        val apiKey = "411536431406e91e4a114b70ae61db3d"
        val url = "https://api.openweathermap.org/data/2.5/weather?q=$cityName&lang=es&appid=$apiKey&units=metric"

        Fuel.get(url).response { _, response, result ->
            result.fold(
                success = { data ->
                    val responseBody = data.decodeToString()
                    val weatherResponse = Gson().fromJson(responseBody, ClimaResponse::class.java)
                    runOnUiThread {
                        binding.txtResultado.text = """
                            Ciudad: ${weatherResponse.name}
                            Temperatura: ${weatherResponse.main.temp}°C
                            Humedad: ${weatherResponse.main.humidity}%
                            Descripción: ${weatherResponse.weather[0].description}
                        """.trimIndent()
                    }
                },
                failure = { error ->
                    runOnUiThread {
                        Toast.makeText(this, "Error fetching weather data: ${error.message}", Toast.LENGTH_LONG).show()
                    }
                }
            )
        }
    }
}