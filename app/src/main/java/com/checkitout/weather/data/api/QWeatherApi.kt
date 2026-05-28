package com.checkitout.weather.data.api

import com.checkitout.weather.data.AuthConstants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface QWeatherApi {

    @GET("geo/v2/city/lookup")
    suspend fun lookupCity(
        @Query("location") location: String
    ): CityResponse

    @GET("v7/weather/now")
    suspend fun getWeatherNow(
        @Query("location") location: String
    ): WeatherNowResponse

    @GET("v7/weather/7d")
    suspend fun getWeather7d(
        @Query("location") location: String
    ): Forecast7dResponse

    @GET("v7/historical/weather")
    suspend fun getHistoricalWeather(
        @Query("location") location: String,
        @Query("date") date: String
    ): HistoricalWeatherResponse

    @GET("airquality/v1/current/{location}")
    suspend fun getAirQuality(
        @Path("location") location: String
    ): AirQualityResponse

    @GET("v7/astronomy/sun")
    suspend fun getSunInfo(
        @Query("location") location: String,
        @Query("date") date: String
    ): SunResponse

    companion object {
        fun create(): QWeatherApi {
            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val client = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val token = QWeatherAuth.getToken()
                    val request = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer $token")
                        .build()
                    chain.proceed(request)
                }
                .addInterceptor(logging)
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .baseUrl(AuthConstants.API_BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(QWeatherApi::class.java)
        }
    }
}
