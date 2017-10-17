package net.techbrewery.weekendowka.document

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.preference.PreferenceManager
import net.techbrewery.weekendowka.base.BundleKey
import net.techbrewery.weekendowka.base.extensions.addOrReplaceFirst
import net.techbrewery.weekendowka.base.extensions.repository
import net.techbrewery.weekendowka.base.extensions.toDateTime
import net.techbrewery.weekendowka.base.network.FirestoreRequestListener
import net.techbrewery.weekendowka.model.*
import org.joda.time.DateTime
import timber.log.Timber

/**
 * Created by Jacek Kwiecień on 13.10.2017.
 */
class DocumentViewModel(application: Application) : AndroidViewModel(application), DocumentMvvm.ViewModel {

    override var company: Company? = null
        set(value) {
            field = value
            val document = Document()
            document.declarer = value?.getSelectedDeclarer()
            document.driver = value?.getSelectedDriver()
            documentLiveData.value = document
        }

    override val eventLiveData: MutableLiveData<DocumentViewEvent> = MutableLiveData()
    override val documentLiveData: MutableLiveData<Document> = MutableLiveData()

    private val repository = application.repository

    override fun getStartDateTime(): DateTime {
        val document = documentLiveData.value
        return document?.actionStartDate?.toDateTime() ?: DateTime.now()
    }

    override fun getStartTime(): Time {
        val document = documentLiveData.value
        val dateTime = document?.actionStartDate?.toDateTime() ?: DateTime.now()
        return Time(dateTime)
    }

    override fun getEndDateTime(): DateTime {
        val document = documentLiveData.value
        return document?.actionEndDate?.toDateTime() ?: DateTime.now()
    }

    override fun getEndTime(): Time {
        val document = documentLiveData.value
        val dateTime = document?.actionEndDate?.toDateTime() ?: DateTime.now()
        return Time(dateTime)
    }

    override fun getDateOfDeclarerSigning(): DateTime {
        val document = documentLiveData.value
        return document?.dateOfDeclarerSigning?.toDateTime() ?: DateTime.now()
    }

    override fun getDateOfDriverSigning(): DateTime {
        val document = documentLiveData.value
        return document?.dateOfDriverSigning?.toDateTime() ?: DateTime.now()
    }

    override fun onStartDatePicked(dateTime: DateTime) {
        val document = documentLiveData.value
        document?.let {
            document.setStartDate(dateTime)
            documentLiveData.postValue(document)
        }
    }

    override fun onStartTimePicked(time: Time) {
        val document = documentLiveData.value
        document?.let {
            document.setStartTime(time)
            documentLiveData.postValue(document)
        }
    }

    override fun onEndDatePicked(dateTime: DateTime) {
        val document = documentLiveData.value
        document?.let {
            document.setEndDate(dateTime)
            documentLiveData.postValue(document)
        }
    }

    override fun onEndTimePicked(time: Time) {
        val document = documentLiveData.value
        document?.let {
            document.setEndTime(time)
            documentLiveData.postValue(document)
        }
    }

    override fun onDateOfDeclarerSigningPicked(dateTime: DateTime) {
        val document = documentLiveData.value
        document?.let {
            document.dateOfDeclarerSigning = dateTime.toDate()
            documentLiveData.postValue(document)
        }
    }

    override fun onDateOfDriverSigningPicked(dateTime: DateTime) {
        val document = documentLiveData.value
        document?.let {
            document.dateOfDriverSigning = dateTime.toDate()
            documentLiveData.postValue(document)
        }
    }

    override fun onDriverActionSelected(action: String) {
        val document = documentLiveData.value
        document?.let { document.driverAction = action }
    }

    override fun onPlaceOfDeclarerSigningChanged(place: String) {
        PreferenceManager.getDefaultSharedPreferences(getApplication()).edit().putString(BundleKey.PLACE_OF_DECLARER_SIGNING, place).apply()
    }

    override fun onPlaceOfDriverSigningChanged(place: String) {
        PreferenceManager.getDefaultSharedPreferences(getApplication()).edit().putString(BundleKey.PLACE_OF_DRIVER_SIGNING, place).apply()
    }

    override fun onSelectedDeclarerChanged(declarer: Declarer) {
        val document = documentLiveData.value
        val company = company
        if (company != null && declarer != company.getSelectedDeclarer()) {
            document?.let {
                document.declarer = declarer
                company.selectedDeclarerId = declarer.id
                company.declarers.addOrReplaceFirst(declarer)
                documentLiveData.postValue(document)

                repository.saveCompany(company, object : FirestoreRequestListener<Company> {
                    override fun onSuccess(responseObject: Company) {}

                    override fun onFailure(error: Throwable) {
                        Timber.e(error, "Changing declarer failed")
                    }
                })
            }
        }
    }

    override fun onSelectedDriverChanged(driver: Driver) {
        val document = documentLiveData.value
        val company = company
        if (company != null && driver != company.getSelectedDriver()) {
            document?.let {
                document.driver = driver
                company.selectedDriverId = driver.id
                company.drivers.addOrReplaceFirst(driver)
                documentLiveData.postValue(document)

                repository.saveCompany(company, object : FirestoreRequestListener<Company> {
                    override fun onSuccess(responseObject: Company) {}

                    override fun onFailure(error: Throwable) {
                        Timber.e(error, "Changing driver failed")
                    }
                })
            }
        }
    }

    override fun saveDocument(placeOfDeclarerSigning: String, placeOfDriverSigning: String) {
        val document = documentLiveData.value
        val company = company

        if (company != null && document != null) {
            document.placeOfDeclarerSigning = placeOfDeclarerSigning
            document.placeOfDriverSigning = placeOfDriverSigning
            document.declarer = company.getSelectedDeclarer()
            document.driver = company.getSelectedDriver()
            repository.saveDocument(company.id, document, object : FirestoreRequestListener<Document> {
                override fun onSuccess(responseObject: Document) {
                    documentLiveData.value = document.copy()
                    eventLiveData.postValue(DocumentViewEvent.DocumentSaved(company, responseObject))
                }

                override fun onFailure(error: Throwable) {
                    eventLiveData.postValue(DocumentViewEvent.Error(error))
                }

            })
        }
    }

}