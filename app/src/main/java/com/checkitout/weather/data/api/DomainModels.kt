package com.checkitout.weather.data.api

data class DomainCity(
    val id: String,
    val name: String,
    val lat: Double,
    val lon: Double,
    val adm1: String = "",
    val adm2: String = ""
)

data class DomainCurrentWeather(
    val temp: Int,
    val feelsLike: Int,
    val icon: String,
    val text: String,
    val windDir: String,
    val windSpeed: Int,
    val humidity: Int,
    val precip: Double,
    val pressure: Int,
    val vis: String,
    val cloud: Int,
    val dew: String,
    val updateTime: String
)

data class DomainForecastDay(
    val date: String,
    val tempMax: Int,
    val tempMin: Int,
    val iconDay: String,
    val textDay: String,
    val iconNight: String = "",
    val textNight: String = "",
    val windDirDay: String = "",
    val windSpeedDay: Int = 0,
    val windDirNight: String = "",
    val windSpeedNight: Int = 0,
    val humidity: Int = 0,
    val precip: Double = 0.0,
    val pressure: Int = 0,
    val uvIndex: Int = 0,
    val vis: String = "0"
)

data class DomainAirQuality(
    val aqi: Int,
    val category: String,
    val primary: String,
    val pm2p5: Double,
    val pm10: Double,
    val no2: Double,
    val so2: Double,
    val co: Double,
    val o3: Double
)

data class DomainSunInfo(
    val sunrise: String,
    val sunset: String
)

data class DomainWeatherData(
    val city: DomainCity,
    val current: DomainCurrentWeather?,
    val forecast: List<DomainForecastDay>,
    val yesterday: DomainForecastDay?,
    val air: DomainAirQuality?,
    val sun: DomainSunInfo?
)
