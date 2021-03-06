package net.techbrewery.weekendowka.onboarding.driver

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.inputmethod.EditorInfo
import kotlinx.android.synthetic.main.activity_driver.*
import kotlinx.android.synthetic.main.view_error.*
import kotlinx.android.synthetic.main.view_progress.*
import net.techbrewery.weekendowka.R
import net.techbrewery.weekendowka.base.BundleKey
import net.techbrewery.weekendowka.base.RequestCode
import net.techbrewery.weekendowka.base.TextChangedListener
import net.techbrewery.weekendowka.base.extensions.hideKeyboard
import net.techbrewery.weekendowka.base.extensions.toDateTime
import net.techbrewery.weekendowka.base.view.DatePickerInput
import net.techbrewery.weekendowka.document.DocumentActivity
import net.techbrewery.weekendowka.model.Company
import org.joda.time.DateTime
import pl.aprilapps.switcher.Switcher
import timber.log.Timber

/**
 * Created by Jacek Kwiecień on 13.10.2017.
 */
class DriverActivity : AppCompatActivity(), DriverMvvm.View {

    companion object {
        fun startToSetupFirst(activity: Activity, company: Company) {
            val intent = Intent(activity, DriverActivity::class.java)
            intent.putExtra(BundleKey.COMPANY, company)
            activity.startActivity(intent)
        }

        fun startToAddNew(activity: Activity, company: Company) {
            val intent = Intent(activity, DriverActivity::class.java)
            intent.putExtra(BundleKey.COMPANY, company)
            intent.putExtra(BundleKey.CREATE_FIRST, false)
            activity.startActivityForResult(intent, RequestCode.CREATE_DRIVER)
        }
    }

    lateinit var viewModel: DriverMvvm.ViewModel
    lateinit var switcher: Switcher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver)
        viewModel = ViewModelProviders.of(this).get(DriverViewModel::class.java)
        viewModel.company = intent.getSerializableExtra(BundleKey.COMPANY) as Company
        viewModel.createFirst = intent.getBooleanExtra(BundleKey.CREATE_FIRST, true)
        setupView()
    }

    override fun setupView() {
        setupSwitcher()
        setupSaveButton()
        setupDismissErrorButton()
        setupEventObserver()
        setupDriverObserver()
        setupBirthdayInput()
        setupEmploymentInput()
        setupNameInput()
        setupIdNumberInput()
    }

    override fun setupSwitcher() {
        switcher = Switcher.Builder(this)
                .addContentView(contentAtDriverActivity)
                .addProgressView(progressView)
                .addErrorView(errorView)
                .setErrorLabel(errorLabel)
                .build()
    }

    override fun setupDismissErrorButton() {
        dismissErrorButton.setOnClickListener { switcher.showContentView() }
    }

    override fun setupSaveButton() {
        saveButtonAtDriverActivity.setOnClickListener { save() }
    }

    override fun setupEventObserver() {
        viewModel.eventLiveData.observe(this, Observer { event ->
            when (event) {
                is DriverViewEvent.DriverSaved -> {
                    DocumentActivity.start(this, event.company)
                    finish()
                }

                is DriverViewEvent.DriverCreated -> {
                    val data = Intent()
                    data.putExtra(BundleKey.DRIVER, event.driver)
                    setResult(Activity.RESULT_OK, data)
                    finish()
                }

                is DriverViewEvent.Error -> {
                    Timber.d(event.error)
                    switcher.showErrorView()
                }
            }
        })
    }

    override fun setupBirthdayInput() {
        birthdayInputAtDriverActivity.datePickerListener = object : DatePickerInput.DatePickerListener {
            override fun provideDate(): DateTime {
                val driver = viewModel.driverLiveData.value
                return driver?.birthday?.toDateTime() ?: DateTime.now()
            }

            override fun onDatePicked(date: DateTime) {
                val driver = viewModel.driverLiveData.value
                driver?.let {
                    driver.birthday = date.toDate()
                    viewModel.driverLiveData.postValue(driver)
                }
            }
        }
    }

    override fun setupEmploymentInput() {
        employmentDateInputAtDriverActivity.datePickerListener = object : DatePickerInput.DatePickerListener {
            override fun provideDate(): DateTime {
                val driver = viewModel.driverLiveData.value
                return driver?.employmentDate?.toDateTime() ?: DateTime.now()
            }

            override fun onDatePicked(date: DateTime) {
                val driver = viewModel.driverLiveData.value
                driver?.let {
                    driver.employmentDate = date.toDate()
                    viewModel.driverLiveData.postValue(driver)
                }
            }
        }
    }

    override fun setupNameInput() {
        nameInputAtDriverActivity.addTextChangedListener(object : TextChangedListener() {
            override fun onTextChanged(newText: String) {
                nameInputLayoutAtDriverActivity.error = null
            }
        })
    }

    override fun setupIdNumberInput() {
        idNumberInputAtDriverActivity.addTextChangedListener(object : TextChangedListener() {
            override fun onTextChanged(newText: String) {
                idNumberInputLayoutAtDriverActivity.error = null
            }
        })

        idNumberInputAtDriverActivity.setOnEditorActionListener { textView, id, keyEvent ->
            id != EditorInfo.IME_ACTION_NEXT
        }
    }

    override fun setupDriverObserver() {
        viewModel.driverLiveData.observe(this, Observer { driver ->
            driver?.let {
                birthdayInputAtDriverActivity.update(driver.birthday.toDateTime())
                employmentDateInputAtDriverActivity.update(driver.employmentDate.toDateTime())
            }
        })
    }

    private fun save() {
        if (validate()) {
            idNumberInputAtDriverActivity.hideKeyboard()
            switcher.showProgressView()
            viewModel.saveDriver(
                    nameInputAtDriverActivity.text.toString(),
                    idNumberInputAtDriverActivity.text.toString()
            )
        }
    }

    private fun validate(): Boolean {
        var valid = true

        if (nameInputAtDriverActivity.text.toString().length < 3) {
            nameInputLayoutAtDriverActivity.error = getString(R.string.error_name_invalid)
            valid = false
        }

        if (idNumberInputAtDriverActivity.text.toString().length < 5) {
            idNumberInputLayoutAtDriverActivity.error = getString(R.string.error_id_number_too_short)
            valid = false
        }

        return valid
    }
}