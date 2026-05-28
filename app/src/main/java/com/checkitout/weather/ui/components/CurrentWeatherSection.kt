package com.checkitout.weather.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.checkitout.weather.data.api.DomainCurrentWeather
import com.checkitout.weather.util.WeatherUtils

@Composable
fun CurrentWeatherSection(
    current: DomainCurrentWeather?,
    locationName: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .animateContentSize()
            .padding(24.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = locationName,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (current != null) {
                Text(
                    text = WeatherUtils.getWeatherEmoji(current.icon),
                    fontSize = 64.sp
                )

                Text(
                    text = "${current.temp}°C",
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "${current.text}  ·  体感 ${current.feelsLike}°C  ·  ☁ ${current.cloud}%",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                val beaufort = WeatherUtils.windBeaufort(current.windSpeed)
                WeatherInfoRow(
                    items = listOf(
                        "💧 湿度 ${current.humidity}%" to true,
                        "🌧 降水 ${current.precip}mm" to true,
                        "🌀 气压 ${current.pressure}hPa" to true,
                        "🌬 风 ${current.windDir}${beaufort}级 (${current.windSpeed}km/h)" to true,
                        "👁 能见度 ${current.vis}km" to true
                    )
                )
            } else {
                Text(
                    text = "暂无天气数据",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun WeatherInfoRow(
    items: List<Pair<String, Boolean>>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val chunks = items.chunked(3)
        for (chunk in chunks) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for ((text, visible) in chunk) {
                    if (visible) {
                        Text(
                            text = text,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
