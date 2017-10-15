package net.techbrewery.weekendowka.document

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import net.techbrewery.weekendowka.base.extensions.repository
import net.techbrewery.weekendowka.base.extensions.toDateTime
import net.techbrewery.weekendowka.base.network.FirestoreRequestListener
import net.techbrewery.weekendowka.model.Company
import net.techbrewery.weekendowka.model.Document
import net.techbrewery.weekendowka.model.Time
import org.joda.time.DateTime

/**
 * Created by Jacek Kwiecień on 13.10.2017.
 */
class DocumentViewModel(application: Application) : AndroidViewModel(application), DocumentMvvm.ViewModel {

    override lateinit var company: Company

    override val eventLiveData: MutableLiveData<DocumentViewEvent> = MutableLiveData()
    override val documentLiveData: MutableLiveData<Document> = MutableLiveData()

    private val repository = application.repository

    init {
        documentLiveData.value = Document()
    }

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

    override fun saveDocument(placeOfDeclarerSigning: String, placeOfDriverSigning: String) {
        val document = documentLiveData.value
        document?.let {
            document.placeOfDeclarerSigning = placeOfDeclarerSigning
            document.placeOfDriverSigning = placeOfDriverSigning
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