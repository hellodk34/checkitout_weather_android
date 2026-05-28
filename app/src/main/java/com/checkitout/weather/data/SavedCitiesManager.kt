package com.checkitout.weather.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.checkitout.weather.data.api.DomainCity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "saved_cities")

class SavedCitiesManager(private val context: Context) {

    private val gson = Gson()
    private val citiesKey = stringPreferencesKey("cities")

    fun getSavedCitiesFlow(): Flow<List<DomainCity>> {
        return context.dataStore.data.map { prefs ->
            val json = prefs[citiesKey] ?: "[]"
            val type = object : TypeToken<List<DomainCity>>() {}.type
            gson.fromJson(json, type) ?: emptyList()
        }
    }

    suspend fun addCity(city: DomainCity) {
        context.dataStore.edit { prefs ->
            val json = prefs[citiesKey] ?: "[]"
            val type = object : TypeToken<MutableList<DomainCity>>() {}.type
            val cities: MutableList<DomainCity> = gson.fromJson(json, type) ?: mutableListOf()
            if (cities.none { it.id == city.id }) {
                cities.add(city)
                prefs[citiesKey] = gson.toJson(cities)
            }
        }
    }

    suspend fun removeCity(cityId: String) {
        context.dataStore.edit { prefs ->
            val json = prefs[citiesKey] ?: "[]"
            val type = object : TypeToken<MutableList<DomainCity>>() {}.type
            val cities: MutableList<DomainCity> = gson.fromJson(json, type) ?: mutableListOf()
            cities.removeAll { it.id == cityId }
            prefs[citiesKey] = gson.toJson(cities)
        }
    }
}
