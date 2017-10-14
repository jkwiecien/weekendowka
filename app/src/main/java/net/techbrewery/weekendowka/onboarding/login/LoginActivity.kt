package net.techbrewery.weekendowka.onboarding.login

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.text.Html
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.view_error.*
import kotlinx.android.synthetic.main.view_progress.*
import net.techbrewery.weekendowka.R
import net.techbrewery.weekendowka.base.view.BaseActivity
import net.techbrewery.weekendowka.document.DocumentActivity
import net.techbrewery.weekendowka.onboarding.company.CompanyActivity
import pl.aprilapps.switcher.Switcher
import timber.log.Timber

/**
 * Created by Jacek Kwiecień on 13.10.2017.
 */

class LoginActivity : BaseActivity(), LoginMvvm.View {

    lateinit var viewModel: LoginMvvm.ViewModel
    lateinit var switcher: Switcher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        setupView()

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            switcher.showProgressViewImmediately()
            viewModel.checkCompany(user.uid)
        }
    }

    private fun setupView() {
        setupSwitcher()
        setupErrorObserver()
        setupUserObserver()
        setupCompanyObserver()
        setupDismissErrorButton()
        setupStartAppButton()
        setupOnboardingMessage(getString(R.string.onboarding))
    }

    override fun setupSwitcher() {
        switcher = Switcher.Builder(this)
                .addContentView(contentAtLoginActivity)
                .addProgressView(progressView)
                .addErrorView(errorView)
                .setErrorLabel(errorLabel)
                .build()
    }

    override fun setupDismissErrorButton() {
        dismissErrorButton.setOnClickListener { finish() }
    }

    override fun setupStartAppButton() {
        startButtonAtOnboardingActivity.setOnClickListener { signIn(true) }
    }

    override fun setupOnboardingMessage(html: String) {
        onboardingLabelAtLoginActivity.text = Html.fromHtml(html)
    }

    override fun setupErrorObserver() {
        viewModel.errorLiveData.observe(this, Observer { error ->
            Timber.e(error)
            switcher.showErrorView()
        })
    }

    override fun setupUserObserver() {
        viewModel.userLiveData.observe(this, Observer {
            CompanyActivity.start(this)
            finish()
        })
    }

    override fun setupCompanyObserver() {
        viewModel.companyLiveData.observe(this, Observer { company ->
            if (company == null) {
                switcher.showContentView()
            } else {
                DocumentActivity.start(this, company)
                finish()
            }
        })
    }

    override fun signIn(firstAttempt: Boolean) {
        if (firstAttempt) switcher.showProgressViewImmediately() else switcher.showProgressView()
        viewModel.signInAnonymously()
    }


}