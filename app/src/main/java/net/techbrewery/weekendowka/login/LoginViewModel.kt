package net.techbrewery.weekendowka.login

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import net.techbrewery.weekendowka.base.extensions.repository
import net.techbrewery.weekendowka.base.network.FirestoreRequestListener

/**
 * Created by Jacek Kwiecień on 13.10.2017.
 */
class LoginViewModel(application: Application) : AndroidViewModel(application), LoginMvvm.ViewModel {

    override val userLiveData: MutableLiveData<FirebaseUser> = MutableLiveData()
    override val errorLiveData: MutableLiveData<Throwable> = MutableLiveData()

    private val repository = application.repository

    override fun signInAnonymously() {
        repository.signInAnonymously(object : FirestoreRequestListener<FirebaseUser> {
            override fun onSuccess(responseObject: FirebaseUser) {
                userLiveData.value = responseObject
            }

            override fun onFailure(error: Throwable) {
                errorLiveData.value = error
            }

        })
    }
}