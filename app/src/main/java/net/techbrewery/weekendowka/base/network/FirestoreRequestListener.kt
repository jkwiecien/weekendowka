package net.techbrewery.weekendowka.base.network

/**
 * Created by Jacek Kwiecień on 13.10.2017.
 */
interface FirestoreRequestListener<T> {

    fun onSuccess(responseObject: T)

    fun onFailure(error: Throwable)

}