package net.techbrewery.weekendowka.onboarding.declarer

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import kotlinx.android.synthetic.main.activity_declarer.*
import kotlinx.android.synthetic.main.view_error.*
import kotlinx.android.synthetic.main.view_progress.*
import net.techbrewery.weekendowka.R
import net.techbrewery.weekendowka.base.BundleKey
import net.techbrewery.weekendowka.base.RequestCode
import net.techbrewery.weekendowka.base.TextChangedListener
import net.techbrewery.weekendowka.base.extensions.hideKeyboard
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
        fun startToSetupFirst(activity: Activity, company: Company) {
            val intent = Intent(activity, DeclarerActivity::class.java)
            intent.putExtra(BundleKey.COMPANY, company)
            activity.startActivity(intent)
        }

        fun startToAddNew(activity: Activity, company: Company) {
            val intent = Intent(activity, DeclarerActivity::class.java)
            intent.putExtra(BundleKey.COMPANY, company)
            intent.putExtra(BundleKey.CREATE_FIRST, false)
            activity.startActivityForResult(intent, RequestCode.CREATE_DECLARER)
        }
    }

    lateinit var viewModel: DeclarerMvvm.ViewModel
    lateinit var switcher: Switcher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_declarer)
        viewModel = ViewModelProviders.of(this).get(DeclarerViewModel::class.java)
        viewModel.company = intent.getSerializableExtra(BundleKey.COMPANY) as Company
        viewModel.createFirst = intent.getBooleanExtra(BundleKey.CREATE_FIRST, true)
        setupView()
    }

    override fun setupView() {
        setupSwitcher()
        setupSaveButton()
        setupDismissErrorButton()
        setupEventObserver()
        setupNameInput()
        setupPositionInput()
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
                    DriverActivity.startToSetupFirst(this, event.company)
                    finish()
                }

                is DeclarerViewEvent.DeclarerCreated -> {
                    val data = Intent()
                    data.putExtra(BundleKey.DECLARER, event.declarer)
                    setResult(Activity.RESULT_OK, data)
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

    override fun setupNameInput() {
        nameInputArDeclarerActivity.addTextChangedListener(object : TextChangedListener() {
            override fun onTextChanged(newText: String) {
                nameInputLayoutArDeclarerActivity.error = null
            }
        })
    }

    override fun setupPositionInput() {
        positionInputArDeclarerActivity.addTextChangedListener(object : TextChangedListener() {
            override fun onTextChanged(newText: String) {
                positionInputLayoutArDeclarerActivity.error = null
            }
        })

        positionInputArDeclarerActivity.setOnEditorActionListener { textView, id, keyEvent ->
            if (id == EditorInfo.IME_ACTION_DONE) {
                save()
                false
            } else {
                true
            }
        }
    }

    private fun save() {
        if (validate()) {
            positionInputArDeclarerActivity.hideKeyboard()
            switcher.showProgressView()
            viewModel.saveDeclarer(
                    nameInputArDeclarerActivity.text.toString(),
                    positionInputArDeclarerActivity.text.toString()
            )
        }
    }

    private fun validate(): Boolean {
        var valid = true

        if (nameInputArDeclarerActivity.text.toString().length < 3) {
            nameInputLayoutArDeclarerActivity.error = getString(R.string.error_name_invalid)
            valid = false
        }

        if (positionInputArDeclarerActivity.text.toString().isBlank()) {
            positionInputLayoutArDeclarerActivity.error = getString(R.string.error_position_empty)
            valid = false
        }

        return valid
    }

}