package net.techbrewery.weekendowka.base.network

import com.google.firebase.auth.FirebaseUser

/**
 * Created by Jacek Kwiecień on 13.10.2017.
 */
interface Repository {
    fun signInAnonymously(requestListener: FirestoreRequestListener<FirebaseUser>)
}