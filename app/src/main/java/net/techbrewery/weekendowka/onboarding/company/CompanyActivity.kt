package net.techbrewery.weekendowka.onboarding.company

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_company.*
import kotlinx.android.synthetic.main.view_error.*
import kotlinx.android.synthetic.main.view_progress.*
import net.techbrewery.weekendowka.R
import net.techbrewery.weekendowka.base.view.BaseActivity
import net.techbrewery.weekendowka.onboarding.declarer.DeclarerActivity
import pl.aprilapps.switcher.Switcher
import timber.log.Timber

/**
 * Created by Jacek Kwiecień on 13.10.2017.
 */
class CompanyActivity : BaseActivity(), CompanyMvvm.View {

    companion object {
        fun start(activity: Activity) {
            val intent = Intent(activity, CompanyActivity::class.java)
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
                DeclarerActivity.start(this, company)
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

    override fun setSaveButtonEnabled(enabled: Boolean) {
        saveButtonAtCompanyActivity.isEnabled = enabled
    }

    private fun save() {
        switcher.showProgressView()
        viewModel.saveCompany(
                nameInputAtCompanyActivity.text.toString(),
                addressInputAtCompanyActivity.text.toString(),
                phoneInputAtCompanyActivity.text.toString(),
                emailInputAtCompanyActivity.text.toString()
        )
    }


    private fun setupView() {
        setupSwitcher()
        setupSaveButton()
        setupDismissErrorButton()
        setupErrorObserver()
        setupCompanyObserver()
    }
}