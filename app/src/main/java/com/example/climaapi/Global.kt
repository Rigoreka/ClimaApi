package com.example.climaapi

data class ClimaResponse(
    val main: Main,
    val weather:List<Weather>,
    val name:String
)

data class Main(
    val temp:Float,
    val humidity:Float
)

data class Weather(
    val description:String
)