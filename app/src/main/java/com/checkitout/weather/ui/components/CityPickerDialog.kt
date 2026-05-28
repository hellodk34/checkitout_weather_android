package com.checkitout.weather.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.checkitout.weather.data.api.DomainCity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityPickerDialog(
    cities: List<DomainCity>,
    onDismiss: () -> Unit,
    onSelect: (DomainCity) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(20.dp),
        title = {
            Text("选择城市", style = MaterialTheme.typography.headlineMedium)
        },
        text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(cities) { city ->
                    val label = buildString {
                        append(city.name)
                        if (city.adm2.isNotEmpty() && city.adm2 != city.name) {
                            append(" - ${city.adm2}")
                        }
                        if (city.adm1.isNotEmpty()) {
                            append(" - ${city.adm1}")
                        }
                    }
                    Surface(
                        onClick = { onSelect(city) },
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        }
    )
}
