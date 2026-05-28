package com.checkitout.weather.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.checkitout.weather.data.api.DomainForecastDay
import com.checkitout.weather.util.WeatherUtils

@Composable
fun ForecastSection(
    forecast: List<DomainForecastDay>,
    yesterday: DomainForecastDay?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = "📅 天气预报",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (yesterday != null) {
                    item {
                        ForecastDayCard(
                            day = yesterday,
                            labelOverride = "昨天",
                            dayOffset = 0
                        )
                    }
                }
                items(forecast) { day ->
                    ForecastDayCard(day)
                }
            }
        }
    }
}

@Composable
private fun ForecastDayCard(
    day: DomainForecastDay,
    labelOverride: String? = null,
    dayOffset: Int = 0
) {
    val offset = if (labelOverride != null) 0 else try {
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
        val today = java.util.Calendar.getInstance()
        val fcDate = java.util.Calendar.getInstance().apply {
            time = sdf.parse(day.date) ?: return@ForecastDayCard
        }
        fcDate.get(java.util.Calendar.DAY_OF_YEAR) - today.get(java.util.Calendar.DAY_OF_YEAR) + 1
    } catch (_: Exception) {
        0
    }

    val isNearby = offset in 1..3
    val dateLabel = labelOverride ?: WeatherUtils.formatDateLabel(day.date, offset)
    val weekday = WeatherUtils.formatWeekday(day.date)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f))
            .then(
                if (isNearby) Modifier.padding(horizontal = 18.dp, vertical = 14.dp)
                else Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
            )
    ) {
        Text(
            text = dateLabel,
            style = if (isNearby) MaterialTheme.typography.bodyLarge else MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            fontWeight = if (isNearby) FontWeight.Bold else FontWeight.SemiBold
        )

        if (weekday.isNotEmpty()) {
            Text(
                text = weekday,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(if (isNearby) 8.dp else 4.dp))

        Text(
            text = WeatherUtils.getWeatherEmoji(day.iconDay),
            fontSize = if (isNearby) 32.sp else 28.sp
        )

        Spacer(modifier = Modifier.height(if (isNearby) 8.dp else 4.dp))

        Text(
            text = "${day.tempMax}°/${day.tempMin}°",
            style = if (isNearby) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = day.textDay,
            style = if (isNearby) MaterialTheme.typography.bodyMedium else MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}
