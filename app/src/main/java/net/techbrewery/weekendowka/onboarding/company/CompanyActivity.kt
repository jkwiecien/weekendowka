package net.techbrewery.weekendowka.onboarding.company

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import kotlinx.android.synthetic.main.activity_company.*
import kotlinx.android.synthetic.main.view_error.*
import kotlinx.android.synthetic.main.view_progress.*
import net.techbrewery.weekendowka.R
import net.techbrewery.weekendowka.base.BundleKey
import net.techbrewery.weekendowka.base.TextChangedListener
import net.techbrewery.weekendowka.base.extensions.hideKeyboard
import net.techbrewery.weekendowka.base.view.BaseActivity
import net.techbrewery.weekendowka.onboarding.declarer.DeclarerActivity
import pl.aprilapps.switcher.Switcher
import timber.log.Timber

/**
 * Created by Jacek Kwiecień on 13.10.2017.
 */
class CompanyActivity : BaseActivity(), CompanyMvvm.View {

    companion object {
        fun start(activity: Activity, email: String) {
            val intent = Intent(activity, CompanyActivity::class.java)
            intent.putExtra(BundleKey.EMAIL, email)
            activity.startActivity(intent)
        }
    }

    lateinit var viewModel: CompanyMvvm.ViewModel
    lateinit var switcher: Switcher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company)
        viewModel = ViewModelProviders.of(this).get(CompanyViewModel::class.java)
        setupView()
    }

    override fun setupSwitcher() {
        switcher = Switcher.Builder(this)
                .addContentView(contentAtCompanyActivity)
                .addProgressView(progressView)
                .addErrorView(errorView)
                .setErrorLabel(errorLabel)
                .build()
    }

    override fun setupErrorObserver() {
        viewModel.errorLiveData.observe(this, Observer { error ->
            Timber.e(error)
            switcher.showErrorView()
        })
    }

    override fun setupCompanyObserver() {
        viewModel.companyLiveData.observe(this, Observer { company ->
            company?.let {
                DeclarerActivity.startToSetupFirst(this, company)
                finish()
            }
        })
    }

    override fun setupDismissErrorButton() {
        dismissErrorButton.setOnClickListener { switcher.showContentView() }
    }

    override fun setupSaveButton() {
        saveButtonAtCompanyActivity.setOnClickListener { save() }
    }

    override fun setupNameInput() {
        nameInputAtCompanyActivity.addTextChangedListener(object : TextChangedListener() {
            override fun onTextChanged(newText: String) {
                nameInputLayoutAtCompanyActivity.isErrorEnabled = false
            }
        })
    }

    override fun setupAddressInput() {
        addressInputAtCompanyActivity.addTextChangedListener(object : TextChangedListener() {
            override fun onTextChanged(newText: String) {
                addressInputLayoutAtCompanyActivity.isErrorEnabled = false
            }
        })
    }

    override fun setupPhoneNumberInput() {
        phoneInputAtCompanyActivity.addTextChangedListener(object : TextChangedListener() {
            override fun onTextChanged(newText: String) {
                phoneInputLayoutAtCompanyActivity.error = null
            }
        })
    }

    override fun setupEmailInput() {
        emailInputAtCompanyActivity.setText(intent.getStringExtra(BundleKey.EMAIL))

        emailInputAtCompanyActivity.addTextChangedListener(object : TextChangedListener() {
            override fun onTextChanged(newText: String) {
                emailInputLayoutAtCompanyActivity.error = null
            }
        })
    }

    private fun save() {
        if (validate()) {
            phoneInputAtCompanyActivity.hideKeyboard()
            switcher.showProgressView()
            viewModel.saveCompany(
                    nameInputAtCompanyActivity.text.toString(),
                    addressInputAtCompanyActivity.text.toString(),
                    phoneInputAtCompanyActivity.text.toString(),
                    emailInputAtCompanyActivity.text.toString()
            )
        }
    }


    private fun validate(): Boolean {
        var valid = true

        if (nameInputAtCompanyActivity.text.toString().isBlank()) {
            nameInputLayoutAtCompanyActivity.error = getString(R.string.error_company_name_empty)
            valid = false
        }

        if (addressInputAtCompanyActivity.text.toString().length < 10) {
            addressInputLayoutAtCompanyActivity.error = getString(R.string.error_company_address_too_short)
            valid = false
        }

        if (phoneInputAtCompanyActivity.text.toString().length < 11) {
            phoneInputLayoutAtCompanyActivity.error = getString(R.string.error_phone_number_too_short)
            valid = false
        }

        val email = emailInputAtCompanyActivity.text.toString()
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInputLayoutAtCompanyActivity.error = getString(R.string.error_email_invalid)
            valid = false
        }

        return valid
    }

    private fun setupView() {
        setupSwitcher()
        setupSaveButton()
        setupDismissErrorButton()
        setupErrorObserver()
        setupCompanyObserver()
        setupNameInput()
        setupAddressInput()
        setupPhoneNumberInput()
        setupEmailInput()
        focusCaptorAtCompanyActivity.requestFocus()
    }
}