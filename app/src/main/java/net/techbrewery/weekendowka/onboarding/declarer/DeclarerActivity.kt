package net.techbrewery.weekendowka.onboarding.declarer

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_declarer.*
import kotlinx.android.synthetic.main.view_error.*
import kotlinx.android.synthetic.main.view_progress.*
import net.techbrewery.weekendowka.R
import net.techbrewery.weekendowka.base.BundleKey
import net.techbrewery.weekendowka.base.view.BaseActivity
import net.techbrewery.weekendowka.model.Company
import net.techbrewery.weekendowka.onboarding.driver.DriverActivity
import pl.aprilapps.switcher.Switcher
import timber.log.Timber

/**
 * Created by Jacek Kwiecień on 13.10.2017.
 */
class DeclarerActivity : BaseActivity(), DeclarerMvvm.View {

    companion object {
        fun start(activity: Activity, company: Company) {
            val intent = Intent(activity, DeclarerActivity::class.java)
            intent.putExtra(BundleKey.COMPANY, company)
            activity.startActivity(intent)
        }
    }

    lateinit var viewModel: DeclarerMvvm.ViewModel
    lateinit var switcher: Switcher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_declarer)
        viewModel = ViewModelProviders.of(this).get(DeclarerViewModel::class.java)
        viewModel.company = intent.getSerializableExtra(BundleKey.COMPANY) as Company
        setupView()
    }

    override fun setupView() {
        setupSwitcher()
        setupSaveButton()
        setupDismissErrorButton()
        setupEventObserver()
    }

    override fun setupSwitcher() {
        switcher = Switcher.Builder(this)
                .addContentView(contentAtDeclarerActivity)
                .addProgressView(progressView)
                .addErrorView(errorView)
                .setErrorLabel(errorLabel)
                .build()
    }

    override fun setupEventObserver() {
        viewModel.eventLiveData.observe(this, Observer { event ->
            when (event) {
                is DeclarerViewEvent.DeclarerSaved -> {
                    DriverActivity.start(this, event.company)
                    finish()
                }

                is DeclarerViewEvent.Error -> {
                    Timber.e(event.error)
                    switcher.showErrorView()
                }
            }
        })
    }

    override fun setupDismissErrorButton() {
        dismissErrorButton.setOnClickListener { switcher.showContentView() }
    }

    override fun setupSaveButton() {
        saveButtonAtDeclarerActivity.setOnClickListener { save() }
    }

    override fun setSaveButtonEnabled(enabled: Boolean) {
        saveButtonAtDeclarerActivity.isEnabled = enabled
    }

    private fun save() {
        switcher.showProgressView()
        viewModel.saveDeclarer(
                nameInputArDeclarerActivity.text.toString(),
                positionInputArDeclarerActivity.text.toString()
        )
    }

}