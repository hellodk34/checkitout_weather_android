package com.checkitout.weather.util

import com.checkitout.weather.data.api.DomainCity

object WeatherUtils {

    fun formatCityLabel(city: DomainCity): String = buildString {
        if (city.adm2.isNotEmpty() && city.adm2 != city.name) {
            append(city.adm2)
            append("-")
        }
        append(city.name)
    }

    fun formatCityDetail(city: DomainCity): String = buildString {
        if (city.adm1.isNotEmpty()) append(city.adm1)
        if (city.adm2.isNotEmpty() && city.adm2 != city.adm1) {
            append(" · ")
            append(city.adm2)
        }
        if (city.name.isNotEmpty() && city.name != city.adm2) {
            append(" · ")
            append(city.name)
        }
    }

    fun formatWeekday(dateStr: String): String {
        if (dateStr.isEmpty()) return ""
        return try {
            val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
            val cal = java.util.Calendar.getInstance().apply {
                time = sdf.parse(dateStr) ?: return ""
            }
            val dayOfWeek = cal.get(java.util.Calendar.DAY_OF_WEEK)
            when (dayOfWeek) {
                java.util.Calendar.MONDAY -> "周一"
                java.util.Calendar.TUESDAY -> "周二"
                java.util.Calendar.WEDNESDAY -> "周三"
                java.util.Calendar.THURSDAY -> "周四"
                java.util.Calendar.FRIDAY -> "周五"
                java.util.Calendar.SATURDAY -> "周六"
                java.util.Calendar.SUNDAY -> "周日"
                else -> ""
            }
        } catch (_: Exception) { "" }
    }

    fun windBeaufort(speedKmh: Int): Int = when {
        speedKmh < 1 -> 0
        speedKmh < 6 -> 1
        speedKmh < 12 -> 2
        speedKmh < 20 -> 3
        speedKmh < 29 -> 4
        speedKmh < 39 -> 5
        speedKmh < 50 -> 6
        speedKmh < 62 -> 7
        speedKmh < 75 -> 8
        speedKmh < 89 -> 9
        speedKmh < 103 -> 10
        speedKmh < 118 -> 11
        else -> 12
    }

    fun extractTime(raw: String): String {
        if (raw.isEmpty()) return "--"
        return try {
            val cleaned = raw.replace("+08:00", "+0800")
            val formats = listOf(
                java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", java.util.Locale.getDefault()),
                java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ", java.util.Locale.getDefault())
            )
            for (fmt in formats) {
                try {
                    val dt = fmt.parse(cleaned)
                    if (dt != null) {
                        val outFmt = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault())
                        return outFmt.format(dt)
                    }
                } catch (_: Exception) {}
            }
            raw.takeLast(5)
        } catch (_: Exception) {
            raw
        }
    }

    fun formatUpdateTime(raw: String): String {
        if (raw.isEmpty()) return ""
        return try {
            val cleaned = raw.replace("+08:00", "+0800")
            val formats = listOf(
                java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ", java.util.Locale.getDefault()),
                java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", java.util.Locale.getDefault())
            )
            for (fmt in formats) {
                try {
                    val dt = fmt.parse(cleaned)
                    if (dt != null) {
                        val outFmt = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault())
                        return outFmt.format(dt)
                    }
                } catch (_: Exception) {}
            }
            raw
        } catch (_: Exception) {
            raw
        }
    }

    fun aqiColor(aqi: Int): Long {
        return when {
            aqi <= 50 -> 0xFF00E400
            aqi <= 100 -> 0xFFFFFF00
            aqi <= 150 -> 0xFFFF7E00
            aqi <= 200 -> 0xFFFF0000
            aqi <= 300 -> 0xFF99004C
            else -> 0xFF7E0023
        }
    }

    fun uvDescription(uv: Int): String {
        return when {
            uv <= 2 -> "$uv (弱)"
            uv <= 5 -> "$uv (中等)"
            uv <= 7 -> "$uv (强)"
            uv <= 10 -> "$uv (很强)"
            else -> "$uv (极强)"
        }
    }

    fun getWeatherEmoji(iconCode: String): String {
        return when (iconCode) {
            "100" -> "\u2600\uFE0F"
            "101", "102", "103" -> "\u26C5"
            "104" -> "\u2601\uFE0F"
            "150", "151", "152", "153" -> "\uD83C\uDF19"
            "300", "301", "302", "303", "304" -> "\u26C8\uFE0F"
            "305", "306", "307", "308", "309" -> "\uD83C\uDF27\uFE0F"
            "310", "311", "312" -> "\uD83C\uDF27\uFE0F"
            "313", "314", "315", "316", "317", "318" -> "\uD83C\uDF27\uFE0F"
            "399" -> "\uD83C\uDF27\uFE0F"
            "400", "401", "402", "403", "404", "405", "406", "407", "408", "409", "410" -> "\uD83C\uDF28\uFE0F"
            "499" -> "\uD83C\uDF28\uFE0F"
            "500", "501", "502", "503", "504", "507", "508", "509", "510", "511", "512", "513", "514", "515" -> "\uD83C\uDF2B\uFE0F"
            "600" -> "\uD83C\uDF2C\uFE0F"
            else -> "\u2601\uFE0F"
        }
    }

    fun formatDateLabel(dateStr: String, dayOffset: Int): String {
        if (dateStr.isEmpty()) return "--"
        return try {
            val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
            val dt = sdf.parse(dateStr) ?: return dateStr
            val cal = java.util.Calendar.getInstance().apply { time = dt }
            val month = cal.get(java.util.Calendar.MONTH) + 1
            val day = cal.get(java.util.Calendar.DAY_OF_MONTH)
            when (dayOffset) {
                1 -> "今天 ${month}月${day}日"
                2 -> "明天 ${month}月${day}日"
                3 -> "后天 ${month}月${day}日"
                else -> "${month}月${day}日"
            }
        } catch (_: Exception) {
            dateStr
        }
    }
}
