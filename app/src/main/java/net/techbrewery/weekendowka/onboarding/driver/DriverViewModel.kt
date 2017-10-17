package net.techbrewery.weekendowka.onboarding.driver

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import net.techbrewery.weekendowka.base.extensions.repository
import net.techbrewery.weekendowka.base.network.FirestoreRequestListener
import net.techbrewery.weekendowka.model.Company
import net.techbrewery.weekendowka.model.Driver

/**
 * Created by Jacek Kwiecień on 13.10.2017.
 */
class DriverViewModel(application: Application) : AndroidViewModel(application), DriverMvvm.ViewModel {

    override lateinit var company: Company
    override var createFirst: Boolean = true

    override val eventLiveData: MutableLiveData<DriverViewEvent> = MutableLiveData()
    override val driverLiveData: MutableLiveData<Driver> = MutableLiveData()

    private val repository = application.repository

    init {
        driverLiveData.value = Driver()
    }

    override fun saveDriver(name: String, idNumber: String) {
        val driver = driverLiveData.value
        driver?.let {
            driver.name = name
            driver.idNumber = idNumber
            company.drivers.add(driver)
            company.selectedDriverId = driver.id

            if (createFirst) {
                repository.saveCompany(company, object : FirestoreRequestListener<Company> {
                    override fun onSuccess(responseObject: Company) {
                        eventLiveData.postValue(DriverViewEvent.DriverSaved(responseObject))
                    }

                    override fun onFailure(error: Throwable) {
                        eventLiveData.postValue(DriverViewEvent.Error(error))
                    }
                })
            } else {
                eventLiveData.postValue(DriverViewEvent.DriverCreated(driver))
            }
        }
    }

}