package net.techbrewery.weekendowka.document

import android.arch.lifecycle.MutableLiveData
import net.techbrewery.weekendowka.model.*
import org.joda.time.DateTime

/**
 * Created by Jacek Kwiecień on 13.10.2017.
 */
interface DocumentMvvm {

    interface View {
        fun setupView()
        fun setupSwitcher()
        fun setupSaveButton()
        fun setupDismissErrorButton()
        fun setupEventObserver()
        fun setupDocumentObserver()
        fun setupRestStartDateInput()
        fun setupRestStartTimeInput()
        fun setupRestEndDateInput()
        fun setupRestEndTimeInput()
        fun setupDriverActionDropdown()
        fun setupDeclarerSigningDateInput()
        fun setupDeclarerSigningPlaceInput()
        fun setupDriverSigningDateInput()
        fun setupDriverSigningPlaceInput()
        fun setupSelectedDeclarerInput()
        fun setupSelectedDriverInput()
        fun setSaveButtonEnabled(enabled: Boolean)
    }

    interface ViewModel {
        var company: Company?
        val eventLiveData: MutableLiveData<DocumentViewEvent>
        val documentLiveData: MutableLiveData<Document>

        fun onStartDatePicked(dateTime: DateTime)
        fun onStartTimePicked(time: Time)
        fun onEndDatePicked(dateTime: DateTime)
        fun setEndDateAsSigningDates()
        fun onEndTimePicked(time: Time)
        fun getStartDateTime(): DateTime
        fun getStartTime(): Time
        fun getEndDateTime(): DateTime
        fun onDateOfDeclarerSigningPicked(dateTime: DateTime)
        fun onDateOfDriverSigningPicked(dateTime: DateTime)
        fun onDriverActionSelected(action: String)
        fun onPlaceOfDeclarerSigningChanged(place: String)
        fun onPlaceOfDriverSigningChanged(place: String)
        fun getDateOfDeclarerSigning(): DateTime
        fun getDateOfDriverSigning(): DateTime
        fun onSelectedDeclarerChanged(declarer: Declarer)
        fun onSelectedDriverChanged(driver: Driver)
        fun getEndTime(): Time
        fun saveDocument(placeOfDeclarerSigning: String, placeOfDriverSigning: String)
    }
}