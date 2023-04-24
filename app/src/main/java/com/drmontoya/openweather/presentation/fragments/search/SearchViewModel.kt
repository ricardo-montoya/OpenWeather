package com.drmontoya.openweather.presentation.fragments.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drmontoya.openweather.data.local.entity.asDomainModel
import com.drmontoya.openweather.domain.Resource
import com.drmontoya.openweather.domain.model.Location
import com.drmontoya.openweather.domain.model.asEntityModel
import com.drmontoya.openweather.domain.repository.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchViewModel
@Inject constructor(
    val repository: LocationRepository
) : ViewModel() {
    private val _searchResults = MutableLiveData<List<Location>>()
    val searchResults: LiveData<List<Location>>
        get() = _searchResults

    private val _savedLocations = MutableLiveData<List<Location>>()
    val savedLocations: LiveData<List<Location>>
        get() = _savedLocations

    private val _errorMesssage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMesssage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    init {
        getSavedLocations()
    }

    fun performSearch(keyword: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                postEmptyList()
                repository.getLocationsByKeyword(keyword).onEach {
                    when (it) {
                        is Resource.Failed -> {
                            _errorMesssage.postValue(it.message)
                            _isLoading.postValue(false)
                        }

                        is Resource.Loading -> {
                            _isLoading.postValue(true)
                            _errorMesssage.postValue("")
                        }

                        is Resource.Success -> {
                            _searchResults.postValue(it.data as List<Location>)
                            _isLoading.postValue(false)
                            _errorMesssage.postValue("")
                        }
                    }
                }.collect()
            }
        }
    }

    fun getSavedLocations() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.getSavedLocations().collect() {
                    _savedLocations.postValue(
                        it.map { it.asDomainModel() } + Location(
                            name = CLEAR_ALL_ITEM,
                            admin1 = null,
                            admin2 = null,
                            admin3 = null,
                            country = null,
                            elevation = null,
                            id = null,
                            latitude = null,
                            longitude = null,
                            population = null,
                            timezone = null
                        )
                    )
                }

            }
        }
    }

    fun postEmptyList() {
        _searchResults.postValue(emptyList())
    }

    fun saveLocationToDatabase(location: Location) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.saveLocationToLocal(location.asEntityModel())
            }
        }
    }

    fun removeSavedLocation(location: Location) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.deleteLocation(location.asEntityModel())
            }
        }
    }
    fun clearAllSavedItems(){
         viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.clearLocationDatabase()
            }
        }
    }

    companion object {
        const val CLEAR_ALL_ITEM = "CLEAR_ALL_ITEM"
    }
}