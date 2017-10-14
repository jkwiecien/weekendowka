package net.techbrewery.weekendowka.document

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_document.*
import kotlinx.android.synthetic.main.view_error.*
import kotlinx.android.synthetic.main.view_progress.*
import net.techbrewery.weekendowka.R
import net.techbrewery.weekendowka.base.BundleKey
import net.techbrewery.weekendowka.base.view.BaseActivity
import net.techbrewery.weekendowka.base.view.DatePickerInput
import net.techbrewery.weekendowka.base.view.TimePickerInput
import net.techbrewery.weekendowka.model.Company
import net.techbrewery.weekendowka.model.Time
import org.joda.time.DateTime
import pl.aprilapps.switcher.Switcher

/**
 * Created by Jacek Kwiecień on 13.10.2017.
 */
class DocumentActivity : BaseActivity(), DocumentMvvm.View {

    companion object {
        fun start(activity: Activity, company: Company) {
            val intent = Intent(activity, DocumentActivity::class.java)
            intent.putExtra(BundleKey.COMPANY, company)
            activity.startActivity(intent)
        }
    }

    lateinit var viewModel: DocumentMvvm.ViewModel
    lateinit var switcher: Switcher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_document)
        viewModel = ViewModelProviders.of(this).get(DocumentViewModel::class.java)
        viewModel.company = intent.getSerializableExtra(BundleKey.COMPANY) as Company
        setupView()
    }

    override fun setupSwitcher() {
        switcher = Switcher.Builder(this)
                .addContentView(contentAtDocumentActivity)
                .addProgressView(progressView)
                .addErrorView(errorView)
                .setErrorLabel(errorLabel)
                .build()
    }

    override fun setupView() {
        setupSwitcher()
        setupEventObserver()
        setupDocumentObserver()
        setupRestStartDateInput()
        setupRestStartTimeInput()
        setupRestEndDateInput()
        setupRestEndTimeInput()
        setupDeclarerSigningDateInput()
        setupDeclarerSigningPlaceInput()
        setupDriverSigningDateInput()
        setupDriverSigningPlaceInput()
        setupSelectedDeclarerInput()
        setupSelectedDriverInput()
    }

    override fun setupEventObserver() {
        viewModel.eventLiveData.observe(this, Observer { event ->

        })
    }

    override fun setupDocumentObserver() {
        viewModel.documentLiveData.observe(this, Observer { document ->
            document?.let {
                driverRestStartDateInputAtDocumentActivity.update(document.actionStartDateTime)
                driverRestStartTimeInputAtDocumentActivity.update(Time(document.actionStartDateTime))
                driverRestEndDateInputAtDocumentActivity.update(document.actionEndDateTime)
                driverRestEndTimeInputAtDocumentActivity.update(Time(document.actionEndDateTime))
                dateOfDeclarerSigningInputAtDocumentActivity.update(document.dateOfDeclarerSigning)
                dateOfDriverSigningInputAtDocumentActivity.update(document.dateOfDriverSigning)
            }
        })
    }

    override fun setupRestStartDateInput() {
        driverRestStartDateInputAtDocumentActivity.datePickerListener = object : DatePickerInput.DatePickerListener {
            override fun provideDate(): DateTime {
                return viewModel.getStartDateTime()
            }

            override fun onDatePicked(date: DateTime) {
                viewModel.onStartDatePicked(date)
            }
        }
    }

    override fun setupRestStartTimeInput() {
        driverRestStartTimeInputAtDocumentActivity.timePickerListener = object : TimePickerInput.TimePickerListener {
            override fun provideTime(): Time {
                return viewModel.getStartTime()
            }

            override fun onTimePicked(time: Time) {
                viewModel.onStartTimePicked(time)
            }
        }
    }

    override fun setupRestEndDateInput() {
        driverRestEndDateInputAtDocumentActivity.datePickerListener = object : DatePickerInput.DatePickerListener {
            override fun provideDate(): DateTime {
                return viewModel.getEndDateTime()
            }

            override fun onDatePicked(date: DateTime) {
                viewModel.onEndDatePicked(date)
            }
        }
    }

    override fun setupRestEndTimeInput() {
        driverRestEndTimeInputAtDocumentActivity.timePickerListener = object : TimePickerInput.TimePickerListener {
            override fun provideTime(): Time {
                return viewModel.getEndTime()
            }

            override fun onTimePicked(time: Time) {
                viewModel.onEndTimePicked(time)
            }
        }
    }

    override fun setupDeclarerSigningDateInput() {
        dateOfDeclarerSigningInputAtDocumentActivity.datePickerListener = object : DatePickerInput.DatePickerListener {
            override fun provideDate(): DateTime {
                return viewModel.getDateOfDeclarerSigning()
            }

            override fun onDatePicked(date: DateTime) {
                viewModel.onDateOfDeclarerSigningPicked(date)
            }
        }
    }

    override fun setupDeclarerSigningPlaceInput() {
        //TODO
    }

    override fun setupDriverSigningDateInput() {
        dateOfDeclarerSigningInputAtDocumentActivity.datePickerListener = object : DatePickerInput.DatePickerListener {
            override fun provideDate(): DateTime {
                return viewModel.getDateOfDriverSigning()
            }

            override fun onDatePicked(date: DateTime) {
                viewModel.onDateOfDriverSigningPicked(date)
            }
        }
    }

    override fun setupDriverSigningPlaceInput() {
        //TODO
    }

    override fun setupSelectedDeclarerInput() {
        val selectedDeclarer = viewModel.company.getSelectedDeclarer()
        selectedDeclarer?.let { declarerInputAtDocumentActivity.setText(selectedDeclarer.name) }

        declarerInputAtDocumentActivity.setOnClickListener {
            //TODO
        }
    }

    override fun setupSelectedDriverInput() {
        val selectedDriver = viewModel.company.getSelectedDriver()
        selectedDriver?.let { driverInputAtDocumentActivity.setText(selectedDriver.name) }

        driverInputAtDocumentActivity.setOnClickListener {
            //TODO
        }
    }

    override fun setSaveButtonEnabled(enabled: Boolean) {
        saveButtonAtDocumentActivity.isEnabled = enabled
    }

    override fun setupDismissErrorButton() {
        dismissErrorButton.setOnClickListener { switcher.showContentView() }
    }

    override fun setupSaveButton() {
        saveButtonAtDocumentActivity.setOnClickListener { save() }
    }

    private fun save() {
        switcher.showProgressView()
        viewModel.saveDocument(
                placeOfSigningInputAtDocumentActivity.text.toString(),
                placeOfDriverSigningInputAtDocumentActivity.text.toString()
        )
    }
}