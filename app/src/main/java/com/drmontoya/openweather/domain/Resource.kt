package com.drmontoya.openweather.domain

sealed class Resource<out T>{
    data class Failed(val message :  String) : Resource<String>()
    data class Success<out T>(val data : T) : Resource<T>()
    object Loading : Resource<Nothing>()
}
