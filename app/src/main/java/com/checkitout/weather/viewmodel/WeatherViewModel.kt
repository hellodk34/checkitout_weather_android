package com.checkitout.weather.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.checkitout.weather.data.SavedCitiesManager
import com.checkitout.weather.data.api.DomainCity
import com.checkitout.weather.data.api.DomainWeatherData
import com.checkitout.weather.data.repository.WeatherRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class UiState {
    data object Idle : UiState()
    data object Loading : UiState()
    data class Success(val data: DomainWeatherData) : UiState()
    data class Error(val message: String) : UiState()
}

data class SearchState(
    val query: String = "",
    val results: List<DomainCity> = emptyList(),
    val isSearching: Boolean = false,
    val showPicker: Boolean = false
)

class WeatherViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = WeatherRepository()
    val savedCitiesManager = SavedCitiesManager(application)

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _searchState = MutableStateFlow(SearchState())
    val searchState: StateFlow<SearchState> = _searchState.asStateFlow()

    val savedCities: StateFlow<List<DomainCity>> =
        savedCitiesManager.getSavedCitiesFlow()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _dialogMessage = MutableStateFlow<String?>(null)
    val dialogMessage: StateFlow<String?> = _dialogMessage.asStateFlow()

    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()

    init {
        viewModelScope.launch {
            savedCitiesManager.getSavedCitiesFlow().collect { cities ->
                if (cities.isNotEmpty() && _uiState.value is UiState.Idle) {
                    loadCity(cities.first())
                }
            }
        }
    }

    fun toggleTheme() {
        _isDarkTheme.value = !_isDarkTheme.value
    }

    fun searchCities(query: String) {
        if (query.isBlank()) {
            _searchState.value = SearchState()
            _dialogMessage.value = "请输入城市名称后搜索"
            return
        }
        _searchState.value = _searchState.value.copy(query = query, isSearching = true)

        viewModelScope.launch {
            try {
                val cities = repository.searchCities(query)
                _searchState.value = _searchState.value.copy(
                    results = cities,
                    isSearching = false,
                    showPicker = cities.size > 1
                )
                if (cities.size == 1) {
                    selectCity(cities.first())
                } else if (cities.isEmpty()) {
                    _uiState.value = UiState.Error("未找到匹配的城市")
                }
            } catch (e: Exception) {
                _searchState.value = _searchState.value.copy(
                    isSearching = false
                )
                _uiState.value = UiState.Error("搜索失败: ${e.message}")
            }
        }
    }

    fun selectCity(city: DomainCity) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            _searchState.value = SearchState()
            try {
                savedCitiesManager.addCity(city)
                val data = repository.fetchWeather(city)
                _uiState.value = UiState.Success(data)
            } catch (e: Exception) {
                _uiState.value = UiState.Error("获取天气数据失败: ${e.message}")
            }
        }
    }

    fun loadCity(city: DomainCity) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val data = repository.fetchWeather(city)
                _uiState.value = UiState.Success(data)
            } catch (e: Exception) {
                _uiState.value = UiState.Error("获取天气数据失败: ${e.message}")
            }
        }
    }

    fun refreshWeather() {
        val current = _uiState.value
        if (current is UiState.Success) {
            val oldUpdateTime = current.data.current?.updateTime
            viewModelScope.launch {
                try {
                    val data = repository.fetchWeather(current.data.city)
                    if (oldUpdateTime != null && data.current?.updateTime == oldUpdateTime) {
                        _dialogMessage.value = "当前天气无更新数据，请稍后再试"
                    } else {
                        _uiState.value = UiState.Success(data)
                    }
                } catch (e: Exception) {
                    _uiState.value = UiState.Error("刷新失败: ${e.message}")
                }
            }
        }
    }

    fun deleteSavedCity(cityId: String) {
        viewModelScope.launch {
            savedCitiesManager.removeCity(cityId)
        }
    }

    fun dismissDialog() {
        _dialogMessage.value = null
    }

    fun dismissPicker() {
        _searchState.value = _searchState.value.copy(showPicker = false)
    }
}
