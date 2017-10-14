package net.techbrewery.weekendowka.onboarding.company

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import net.techbrewery.weekendowka.base.extensions.repository
import net.techbrewery.weekendowka.base.network.FirestoreRequestListener
import net.techbrewery.weekendowka.model.Company
import timber.log.Timber

/**
 * Created by Jacek Kwiecień on 13.10.2017.
 */
class CompanyViewModel(application: Application) : AndroidViewModel(application), CompanyMvvm.ViewModel {

    override val companyLiveData: MutableLiveData<Company> = MutableLiveData()
    override val errorLiveData: MutableLiveData<Throwable> = MutableLiveData()

    private val repository = application.repository

    override fun saveCompany(name: String, address: String, phone: String, email: String) {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val company = Company(user.uid, name, address, phone, email)
            repository.saveCompany(company, object : FirestoreRequestListener<Company> {
                override fun onSuccess(responseObject: Company) {
                    companyLiveData.value = responseObject
                }

                override fun onFailure(error: Throwable) {
                    errorLiveData.value = error
                }

            })
        } else {
            errorLiveData.value = NullPointerException("Firebase user is null")
        }
    }
}