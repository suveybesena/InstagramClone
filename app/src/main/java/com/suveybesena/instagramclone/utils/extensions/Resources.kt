package com.suveybesena.instagramclone.utils.extensions

sealed class Resources <T> (
    val data :T? = null,
    val message : String? =null
        ) {
    class Success <T>(data: T) :Resources<T>(data)
    class error<T>(message: String, data: T? = null) : Resources<T>(data, message)
    class loading<T> : Resources<T>()

}