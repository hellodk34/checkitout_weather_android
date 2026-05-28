package com.checkitout.weather.data.api

import com.google.gson.annotations.SerializedName

data class CityResponse(
    val code: String,
    val location: List<CityInfo>?
)

data class CityInfo(
    val id: String,
    val name: String,
    val lat: String,
    val lon: String,
    val adm1: String?,
    val adm2: String?
)

data class WeatherNowResponse(
    val code: String,
    @SerializedName("updateTime") val updateTime: String,
    val now: NowWeather?
)

data class NowWeather(
    @SerializedName("temp") val temp: String,
    @SerializedName("feelsLike") val feelsLike: String,
    @SerializedName("icon") val icon: String,
    @SerializedName("text") val text: String,
    @SerializedName("windDir") val windDir: String,
    @SerializedName("windSpeed") val windSpeed: String,
    @SerializedName("humidity") val humidity: String,
    @SerializedName("precip") val precip: String,
    @SerializedName("pressure") val pressure: String,
    @SerializedName("vis") val vis: String,
    @SerializedName("cloud") val cloud: String,
    @SerializedName("dew") val dew: String
)

data class Forecast7dResponse(
    val code: String,
    val daily: List<DailyForecast>?
)

data class DailyForecast(
    @SerializedName("fxDate") val fxDate: String,
    @SerializedName("tempMax") val tempMax: String,
    @SerializedName("tempMin") val tempMin: String,
    @SerializedName("iconDay") val iconDay: String,
    @SerializedName("textDay") val textDay: String,
    @SerializedName("iconNight") val iconNight: String,
    @SerializedName("textNight") val textNight: String,
    @SerializedName("windDirDay") val windDirDay: String?,
    @SerializedName("windSpeedDay") val windSpeedDay: String?,
    @SerializedName("windDirNight") val windDirNight: String?,
    @SerializedName("windSpeedNight") val windSpeedNight: String?,
    @SerializedName("humidity") val humidity: String?,
    @SerializedName("precip") val precip: String?,
    @SerializedName("pressure") val pressure: String?,
    @SerializedName("uvIndex") val uvIndex: String?,
    @SerializedName("vis") val vis: String?
)

data class HistoricalWeatherResponse(
    val code: String,
    val weatherDaily: HistoricalDaily?,
    val weatherHourly: List<HistoricalHourly>?
)

data class HistoricalDaily(
    val date: String?,
    @SerializedName("tempMax") val tempMax: String?,
    @SerializedName("tempMin") val tempMin: String?,
    val humidity: String?,
    val precip: String?,
    val pressure: String?
)

data class HistoricalHourly(
    val icon: String?
)

data class AirQualityResponse(
    val code: String,
    val indexes: List<AirIndex>?,
    val pollutants: List<Pollutant>?
)

data class AirIndex(
    val code: String?,
    val aqi: Int?,
    val category: String?,
    val primaryPollutant: PrimaryPollutant?
)

data class PrimaryPollutant(
    val name: String?
)

data class Pollutant(
    val code: String?,
    val concentration: Concentration?
)

data class Concentration(
    val value: Double?
)

data class SunResponse(
    val code: String,
    val sunrise: String?,
    val sunset: String?
)
