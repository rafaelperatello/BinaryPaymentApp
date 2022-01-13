package com.penguinpay.data

sealed class Resource<T> {

    class Success<T>(val data: T) : Resource<T>()

    class ErrorNetwork<T>() : Resource<T>()

    class ErrorEmptyData<T>() : Resource<T>()

    // Todo add more errors?
    //    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
}


// Todo remove?
//data class Resource<T>(
//    val status: Status,
//    val data: T? = null,
//) {
//
//    enum class Status {
//        SUCCESS,
//        ERROR
//    }
//
//    companion object {
//        fun <T> success(data: T): Resource<T> {
//            return Resource(Status.SUCCESS, data)
//        }
//
//        fun <T> error(message: String, data: T? = null): Resource<T> {
//            return Resource(Status.ERROR, data)
//        }
//    }
//}

//sealed class Resource<T>(
//    val data: T? = null,
//    val message: String? = null
//) {
//
//    class Success<T>(data: T) : Resource<T>(data)
//
//    class ErrorNetwork<T>(message: String?) : Resource<T>(message = message)
//
//    class ErrorEmptyData<T>() : Resource<T>()
//
//    // Todo add more errors?
//    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
//}
