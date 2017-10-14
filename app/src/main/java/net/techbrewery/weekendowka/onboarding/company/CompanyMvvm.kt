package net.techbrewery.weekendowka.onboarding.company

import android.arch.lifecycle.MutableLiveData
import net.techbrewery.weekendowka.model.Company

/**
 * Created by Jacek Kwiecień on 13.10.2017.
 */
interface CompanyMvvm {

    interface View {
        fun setupSwitcher()
        fun setupSaveButton()
        fun setupDismissErrorButton()
        fun setupErrorObserver()
        fun setupCompanyObserver()
        fun setSaveButtonEnabled(enabled: Boolean)
    }

    interface ViewModel {
        val companyLiveData: MutableLiveData<Company>
        val errorLiveData: MutableLiveData<Throwable>

        fun saveCompany(name: String, address: String, phone: String, email: String)
    }
}