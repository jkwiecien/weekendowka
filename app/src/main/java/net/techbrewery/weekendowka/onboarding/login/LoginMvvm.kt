package net.techbrewery.weekendowka.onboarding.login

import android.arch.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import net.techbrewery.weekendowka.model.Company

/**
 * Created by Jacek Kwiecień on 13.10.2017.
 */
interface LoginMvvm {

    interface View {
        fun setupSwitcher()
        fun setupDismissErrorButton()
        fun setupStartAppButton()
        fun setupOnboardingMessage(html: String)
        fun setupUserObserver()
        fun setupCompanyObserver()
        fun setupErrorObserver()
        fun signIn(firstAttempt: Boolean)
    }

    interface ViewModel {
        val userLiveData: MutableLiveData<FirebaseUser>
        val companyLiveData: MutableLiveData<Company>
        val errorLiveData: MutableLiveData<Throwable>

        fun signInAnonymously()
        fun checkCompany(userId: String)
    }

}