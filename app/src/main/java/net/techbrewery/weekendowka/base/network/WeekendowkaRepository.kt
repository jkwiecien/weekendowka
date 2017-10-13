package net.techbrewery.weekendowka.base.network

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import timber.log.Timber

/**
 * Created by Jacek Kwiecień on 13.10.2017.
 */
class WeekendowkaRepository : Repository {

    override fun signInAnonymously(requestListener: FirestoreRequestListener<FirebaseUser>) {
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signInAnonymously().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                requestListener.onSuccess(task.result.user)
                Timber.d("Signed in annonymously %s", task.result.user.toString())
            } else {
                requestListener.onFailure(task.exception ?: NullPointerException("Exception without exception... this shouldn't happen... like ever"))
                Timber.e(task.exception, "Error signing in annonymously")
            }
        }
    }
}