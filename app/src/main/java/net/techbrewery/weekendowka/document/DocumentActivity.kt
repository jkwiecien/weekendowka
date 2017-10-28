package net.techbrewery.weekendowka.document

import android.Manifest
import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_document.*
import kotlinx.android.synthetic.main.view_error.*
import kotlinx.android.synthetic.main.view_progress.*
import net.techbrewery.weekendowka.R
import net.techbrewery.weekendowka.base.BundleKey
import net.techbrewery.weekendowka.base.RequestCode
import net.techbrewery.weekendowka.base.extensions.sharedPreferences
import net.techbrewery.weekendowka.base.extensions.toDateTime
import net.techbrewery.weekendowka.base.view.BaseActivity
import net.techbrewery.weekendowka.base.view.DatePickerInput
import net.techbrewery.weekendowka.base.view.TimePickerInput
import net.techbrewery.weekendowka.model.Company
import net.techbrewery.weekendowka.model.Declarer
import net.techbrewery.weekendowka.model.Driver
import net.techbrewery.weekendowka.model.Time
import net.techbrewery.weekendowka.people.DeclarersActivity
import net.techbrewery.weekendowka.people.DriversActivity
import org.joda.time.DateTime
import pl.aprilapps.switcher.Switcher
import timber.log.Timber


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
        setupDriverActionDropdown()
        setupDeclarerSigningDateInput()
        setupDeclarerSigningPlaceInput()
        setupDriverSigningDateInput()
        setupDriverSigningPlaceInput()
        setupSelectedDeclarerInput()
        setupSelectedDriverInput()
        setupSaveButton()

        focusCaptorAtDocumentActivity.requestFocus()
    }

    override fun setupEventObserver() {
        viewModel.eventLiveData.observe(this, Observer { event ->
            when (event) {
                is DocumentViewEvent.DocumentSaved -> {
                    val pdfCreator = PdfCreator(this)
                    val pdfFile = pdfCreator.createDocument(event.company, event.document)
                    pdfFile?.let {
                        val uri = FileProvider.getUriForFile(this, applicationContext.packageName + ".file.provider", pdfFile)
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.setDataAndType(uri, "application/pdf")
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        startActivity(intent)
                        switcher.showContentView()
                    }
                }
                is DocumentViewEvent.Error -> {
                    Timber.e(event.error)
                    switcher.showErrorView()
                }
                is DocumentViewEvent.EndDateSet -> {
                    AlertDialog.Builder(this)
                            .setMessage(getString(R.string.end_Date_set_dialog_message))
                            .setPositiveButton(getString(R.string.Yes), { dialogInterface, i -> viewModel.setEndDateAsSigningDates() })
                            .setNegativeButton(getString(R.string.No), null)
                            .create().show()
                }
            }
        })
    }

    override fun setupDocumentObserver() {
        viewModel.documentLiveData.observe(this, Observer { document ->
            document?.let {
                declarerInputAtDocumentActivity.setText(document.declarer?.name)
                driverInputAtDocumentActivity.setText(document.driver?.name)
                driverRestStartDateInputAtDocumentActivity.update(document.actionStartDate.toDateTime())
                driverRestStartTimeInputAtDocumentActivity.update(Time(document.actionStartDate.toDateTime()))
                driverRestEndDateInputAtDocumentActivity.update(document.actionEndDate.toDateTime())
                driverRestEndTimeInputAtDocumentActivity.update(Time(document.actionEndDate.toDateTime()))
                dateOfDeclarerSigningInputAtDocumentActivity.update(document.dateOfDeclarerSigning.toDateTime())
                dateOfDriverSigningInputAtDocumentActivity.update(document.dateOfDriverSigning.toDateTime())
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

    override fun setupDriverSigningDateInput() {
        dateOfDriverSigningInputAtDocumentActivity.datePickerListener = object : DatePickerInput.DatePickerListener {
            override fun provideDate(): DateTime {
                return viewModel.getDateOfDriverSigning()
            }

            override fun onDatePicked(date: DateTime) {
                viewModel.onDateOfDriverSigningPicked(date)
            }
        }
    }

    override fun setupDriverActionDropdown() {
        val adapter = ArrayAdapter.createFromResource(this, R.array.driver_actions, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        driverActionDropdownAtDocumentActivity.adapter = adapter
        driverActionDropdownAtDocumentActivity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(adapterView: AdapterView<*>, parentView: View?, position: Int, id: Long) {
                val adapter = adapterView.adapter as ArrayAdapter<String>
                viewModel.onDriverActionSelected(adapter.getItem(position))
            }

        }
    }

    override fun setupDeclarerSigningPlaceInput() {
        placeOfDeclarerSigningInputAtDocumentActivity.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {
                viewModel.onPlaceOfDeclarerSigningChanged(charSequence.toString())
            }
        })
        placeOfDeclarerSigningInputAtDocumentActivity.setText(sharedPreferences.getString(BundleKey.PLACE_OF_DECLARER_SIGNING, ""))
    }


    override fun setupDriverSigningPlaceInput() {
        placeOfDriverSigningInputAtDocumentActivity.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {
                viewModel.onPlaceOfDriverSigningChanged(charSequence.toString())
            }
        })
        placeOfDriverSigningInputAtDocumentActivity.setText(sharedPreferences.getString(BundleKey.PLACE_OF_DRIVER_SIGNING, ""))
    }

    override fun setupSelectedDeclarerInput() {
        declarerInputAtDocumentActivity.setOnClickListener {
            val company = viewModel.company
            company?.let { DeclarersActivity.start(this, company) }
        }
    }

    override fun setupSelectedDriverInput() {
        driverInputAtDocumentActivity.setOnClickListener {
            val company = viewModel.company
            company?.let { DriversActivity.start(this, company) }
        }
    }

    override fun setSaveButtonEnabled(enabled: Boolean) {
        saveButtonAtDocumentActivity.isEnabled = enabled
    }

    override fun setupDismissErrorButton() {
        dismissErrorButton.setOnClickListener { switcher.showContentView() }
    }

    override fun setupSaveButton() {
        saveButtonAtDocumentActivity.setOnClickListener {
            if (isWritePermissionGranted()) {
                save()
            } else {
                requestWritePermission()
            }
        }
    }

    private fun save() {
        if (validate()) {
            switcher.showProgressView()
            viewModel.saveDocument(
                    placeOfDeclarerSigningInputAtDocumentActivity.text.toString(),
                    placeOfDriverSigningInputAtDocumentActivity.text.toString()
            )
        }
    }

    private fun isWritePermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestWritePermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), RequestCode.REQUEST_WRITE_PERMISSION)
    }

    private fun validate(): Boolean {
        var valid = true

        if (placeOfDeclarerSigningInputAtDocumentActivity.text.toString().isBlank()) {
            placeOfDeclarerSigningInputLayoutAtDocumentActivity.error = getString(R.string.error_place_of_signing_empty)
            valid = false
        }

        if (placeOfDriverSigningInputAtDocumentActivity.text.toString().isBlank()) {
            placeOfDriverSigningInputLayoutAtDocumentActivity.error = getString(R.string.error_place_of_signing_empty)
            valid = false
        }

        return valid
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == RequestCode.SELECT_DECLARER && data != null) {
            val declarer = data.getSerializableExtra(BundleKey.DECLARER) as Declarer
            viewModel.onSelectedDeclarerChanged(declarer)
        } else if (resultCode == Activity.RESULT_OK && requestCode == RequestCode.SELECT_DRIVER && data != null) {
            val driver = data.getSerializableExtra(BundleKey.DRIVER) as Driver
            viewModel.onSelectedDriverChanged(driver)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == RequestCode.REQUEST_WRITE_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                save()
            } else {
                //TODO
            }
        }
    }
}