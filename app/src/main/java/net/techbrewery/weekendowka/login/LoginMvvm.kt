package net.techbrewery.weekendowka.login

import android.arch.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser

/**
 * Created by Jacek Kwiecień on 13.10.2017.
 */
interface LoginMvvm {

    interface View {
        fun setupSwitcher()
        fun setupDismissErrorButton(title: String)
        fun setupUserObserver()
        fun setupErrorObserver()
        fun signIn(firstAttempt: Boolean)
    }

    interface ViewModel {
        val userLiveData: MutableLiveData<FirebaseUser>
        val errorLiveData: MutableLiveData<Throwable>

        fun signInAnonymously()
    }

}