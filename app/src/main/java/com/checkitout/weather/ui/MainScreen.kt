package com.checkitout.weather.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.checkitout.weather.ui.screens.WeatherScreen
import com.checkitout.weather.ui.theme.CheckitoutWeatherTheme
import com.checkitout.weather.viewmodel.WeatherViewModel

@Composable
fun MainScreen(viewModel: WeatherViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val searchState by viewModel.searchState.collectAsStateWithLifecycle()
    val savedCities by viewModel.savedCities.collectAsStateWithLifecycle()
    val isDarkTheme by viewModel.isDarkTheme.collectAsStateWithLifecycle()
    val dialogMessage by viewModel.dialogMessage.collectAsStateWithLifecycle()

    CheckitoutWeatherTheme(darkTheme = isDarkTheme) {
        WeatherScreen(
            uiState = uiState,
            searchQuery = searchState.query,
            savedCities = savedCities,
            isDarkTheme = isDarkTheme,
            searchResults = searchState.results,
            showCityPicker = searchState.showPicker,
            dialogMessage = dialogMessage,
            onSearchQueryChange = { viewModel.searchCities(it) },
            onSearch = { viewModel.searchCities(searchState.query) },
            onCitySelected = { city -> viewModel.selectCity(city) },
            onDeleteCity = { cityId -> viewModel.deleteSavedCity(cityId) },
            onRefresh = { viewModel.refreshWeather() },
            onToggleTheme = { viewModel.toggleTheme() },
            onDismissPicker = { viewModel.dismissPicker() },
            onDismissDialog = { viewModel.dismissDialog() }
        )
    }
}
