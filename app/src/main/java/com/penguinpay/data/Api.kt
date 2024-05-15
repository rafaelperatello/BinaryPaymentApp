package com.penguinpay.data

import android.util.Log
import com.penguinpay.BuildConfig
import retrofit2.Response

object Constants {
    const val BASE_URL = "https://openexchangerates.org/"
    const val API_KEY = BuildConfig.API_KEY
}

/**
 * Result from remote data source
 */
sealed class NetworkResult<T>(
    val data: T? = null,
    val message: String? = null
) {

    class Success<T>(data: T) : NetworkResult<T>(data)

    class Error<T>(message: String) : NetworkResult<T>(message = message)
}

/**
 * Map NetworkResult into Resource
 */
fun <T> NetworkResult<T>.toResource(): Resource<T> {
    return if (this is NetworkResult.Success) {
        if (data == null) {
            Log.e("Mapping to resource", "Data is empty")
            Resource.ErrorEmptyData()
        } else {
            Resource.SuccessData<T>(data)
        }
    } else {
        Log.e("Mapping to resource", "Error fetching data: $message")
        Resource.ErrorNetwork()
    }
}

/**
 * Handle response from api call
 */
suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): NetworkResult<T> {
    try {
        val response = apiCall()
        if (response.isSuccessful) {
            val body = response.body()
            body?.let {
                return NetworkResult.Success(body)
            }
        }
        return NetworkResult.Error("${response.code()} ${response.message()}")
    } catch (e: Exception) {
        return NetworkResult.Error(e.message ?: e.toString())
    }
}