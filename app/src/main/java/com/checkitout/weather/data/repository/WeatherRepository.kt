package com.checkitout.weather.data.repository

import com.checkitout.weather.data.api.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class WeatherRepository(
    private val api: QWeatherApi = QWeatherApi.create()
) {
    suspend fun searchCities(query: String): List<DomainCity> {
        val response = api.lookupCity(query)
        if (response.code != "200") return emptyList()
        return response.location?.map { loc ->
            DomainCity(
                id = loc.id,
                name = loc.name,
                lat = loc.lat.toDoubleOrNull() ?: 0.0,
                lon = loc.lon.toDoubleOrNull() ?: 0.0,
                adm1 = loc.adm1 ?: "",
                adm2 = loc.adm2 ?: ""
            )
        } ?: emptyList()
    }

    suspend fun fetchWeather(city: DomainCity): DomainWeatherData {
        val nowResp = api.getWeatherNow(city.id)
        val fcResp = api.getWeather7d(city.id)

        val current = nowResp.now?.let { now ->
            DomainCurrentWeather(
                temp = now.temp.toIntOrNull() ?: 0,
                feelsLike = now.feelsLike.toIntOrNull() ?: 0,
                icon = now.icon,
                text = WEATHER_CODE_MAP[now.icon] ?: now.text,
                windDir = now.windDir,
                windSpeed = now.windSpeed.toIntOrNull() ?: 0,
                humidity = now.humidity.toIntOrNull() ?: 0,
                precip = now.precip.toDoubleOrNull() ?: 0.0,
                pressure = now.pressure.toIntOrNull() ?: 0,
                vis = now.vis,
                cloud = now.cloud.toIntOrNull() ?: 0,
                dew = now.dew,
                updateTime = nowResp.updateTime
            )
        }

        val forecast = fcResp.daily?.map { day ->
            DomainForecastDay(
                date = day.fxDate,
                tempMax = day.tempMax.toIntOrNull() ?: 0,
                tempMin = day.tempMin.toIntOrNull() ?: 0,
                iconDay = day.iconDay,
                textDay = WEATHER_CODE_MAP[day.iconDay] ?: day.textDay,
                iconNight = day.iconNight,
                textNight = WEATHER_CODE_MAP[day.iconNight] ?: day.textNight,
                windDirDay = day.windDirDay ?: "",
                windSpeedDay = day.windSpeedDay?.toIntOrNull() ?: 0,
                windDirNight = day.windDirNight ?: "",
                windSpeedNight = day.windSpeedNight?.toIntOrNull() ?: 0,
                humidity = day.humidity?.toIntOrNull() ?: 0,
                precip = day.precip?.toDoubleOrNull() ?: 0.0,
                pressure = day.pressure?.toIntOrNull() ?: 0,
                uvIndex = day.uvIndex?.toIntOrNull() ?: 0,
                vis = day.vis ?: "0"
            )
        } ?: emptyList()

        val yesterday = try {
            val yesterday = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"))
            val hist = api.getHistoricalWeather(city.id, yesterday)
            if (hist.code == "200") {
                val daily = hist.weatherDaily
                val icon = hist.weatherHourly?.firstOrNull()?.icon ?: ""
                if (daily != null) {
                    DomainForecastDay(
                        date = daily.date ?: "",
                        tempMax = daily.tempMax?.toIntOrNull() ?: 0,
                        tempMin = daily.tempMin?.toIntOrNull() ?: 0,
                        iconDay = icon,
                        textDay = WEATHER_CODE_MAP[icon] ?: "",
                        humidity = daily.humidity?.toIntOrNull() ?: 0,
                        precip = daily.precip?.toDoubleOrNull() ?: 0.0,
                        pressure = daily.pressure?.toIntOrNull() ?: 0
                    )
                } else null
            } else null
        } catch (_: Exception) {
            null
        }

        val air = try {
            val airResp = api.getAirQuality("${city.lat},${city.lon}")
            if (airResp.code == "200") {
                val idx = airResp.indexes?.firstOrNull { it.code == "us-epa" }
                    ?: airResp.indexes?.firstOrNull()
                val polMap = airResp.pollutants?.associate {
                    (it.code ?: "") to (it.concentration?.value ?: 0.0)
                } ?: emptyMap()
                if (idx != null) {
                    DomainAirQuality(
                        aqi = idx.aqi ?: 0,
                        category = idx.category ?: "",
                        primary = (idx.primaryPollutant?.name) ?: "",
                        pm2p5 = polMap["pm2p5"] ?: 0.0,
                        pm10 = polMap["pm10"] ?: 0.0,
                        no2 = polMap["no2"] ?: 0.0,
                        so2 = polMap["so2"] ?: 0.0,
                        co = polMap["co"] ?: 0.0,
                        o3 = polMap["o3"] ?: 0.0
                    )
                } else null
            } else null
        } catch (_: Exception) {
            null
        }

        val sun = try {
            val today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
            val sunResp = api.getSunInfo(city.id, today)
            if (sunResp.code == "200") {
                DomainSunInfo(
                    sunrise = sunResp.sunrise ?: "",
                    sunset = sunResp.sunset ?: ""
                )
            } else null
        } catch (_: Exception) {
            null
        }

        return DomainWeatherData(
            city = city,
            current = current,
            forecast = forecast,
            yesterday = yesterday,
            air = air,
            sun = sun
        )
    }

    companion object {
        val WEATHER_CODE_MAP = mapOf(
            "100" to "晴", "101" to "多云", "102" to "少云", "103" to "晴间多云",
            "104" to "阴", "300" to "阵雨", "301" to "强阵雨", "302" to "雷阵雨",
            "303" to "强雷阵雨", "304" to "雷阵雨伴有冰雹",
            "305" to "小雨", "306" to "中雨", "307" to "大雨", "308" to "极端降雨",
            "309" to "毛毛雨/细雨", "310" to "暴雨", "311" to "大暴雨",
            "312" to "特大暴雨", "313" to "冻雨", "314" to "小到中雨",
            "315" to "中到大雨", "316" to "大到暴雨", "317" to "暴雨到大暴雨",
            "318" to "大暴雨到特大暴雨",
            "399" to "雨",
            "400" to "小雪", "401" to "中雪", "402" to "大雪", "403" to "暴雪",
            "404" to "雨夹雪", "405" to "雨雪天气", "406" to "阵雨夹雪",
            "407" to "阵雪", "408" to "小到中雪", "409" to "中到大雪",
            "410" to "大到暴雪", "499" to "雪",
            "500" to "薄雾", "501" to "雾", "502" to "霾", "503" to "扬沙",
            "504" to "浮尘", "507" to "沙尘暴", "508" to "强沙尘暴",
            "509" to "浓雾", "510" to "强浓雾", "511" to "中度霾",
            "512" to "重度霾", "513" to "严重霾", "514" to "大雾",
            "515" to "特强浓雾",
            "600" to "热"
        )
    }
}
