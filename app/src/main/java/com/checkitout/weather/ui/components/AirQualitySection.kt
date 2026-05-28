package com.checkitout.weather.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.checkitout.weather.data.api.DomainAirQuality
import com.checkitout.weather.util.WeatherUtils

@Composable
fun AirQualitySection(
    air: DomainAirQuality?,
    modifier: Modifier = Modifier
) {
    if (air == null) return

    val aqiColor = Color(WeatherUtils.aqiColor(air.aqi))

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = "🌬 空气质量",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            val primaryStr = if (air.primary.isNotEmpty()) " 首要污染物: ${air.primary}" else ""

            val richText = buildAnnotatedString {
                withStyle(style = SpanStyle(color = aqiColor, fontWeight = FontWeight.Bold)) {
                    append("${air.category}  (AQI ${air.aqi})")
                }
                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurfaceVariant)) {
                    append("  PM2.5: ${air.pm2p5} μg/m³  PM10: ${air.pm10} μg/m³$primaryStr")
                }
            }

            Text(
                text = richText,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
