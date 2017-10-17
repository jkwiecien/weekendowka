package net.techbrewery.weekendowka.onboarding.driver

import android.arch.lifecycle.MutableLiveData
import net.techbrewery.weekendowka.model.Company
import net.techbrewery.weekendowka.model.Driver

/**
 * Created by Jacek Kwiecień on 13.10.2017.
 */
interface DriverMvvm {

    interface View {
        fun setupView()
        fun setupSwitcher()
        fun setupSaveButton()
        fun setupDismissErrorButton()
        fun setupEventObserver()
        fun setupDriverObserver()
        fun setupBirthdayInput()
        fun setupEmploymentInput()
        fun setSaveButtonEnabled(enabled: Boolean)
    }

    interface ViewModel {
        var company: Company
        var createFirst: Boolean
        val eventLiveData: MutableLiveData<DriverViewEvent>
        val driverLiveData: MutableLiveData<Driver>

        fun saveDriver(name: String, idNumber: String)
    }
}