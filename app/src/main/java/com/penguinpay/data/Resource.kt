package com.penguinpay.data

sealed class Resource<T> {

    class Success<T>() : Resource<T>()

    class SuccessData<T>(val data: T) : Resource<T>()

    class ErrorNetwork<T>() : Resource<T>()

    class ErrorEmptyData<T>() : Resource<T>()

}