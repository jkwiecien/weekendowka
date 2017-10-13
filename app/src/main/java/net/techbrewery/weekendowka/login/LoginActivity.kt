package net.techbrewery.weekendowka.login

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.view_error.*
import kotlinx.android.synthetic.main.view_progress.*
import net.techbrewery.weekendowka.R
import net.techbrewery.weekendowka.base.view.BaseActivity
import net.techbrewery.weekendowka.navigation.NavigationActivity
import pl.aprilapps.switcher.Switcher

/**
 * Created by Jacek Kwiecień on 13.10.2017.
 */

class LoginActivity : BaseActivity(), LoginMvvm.View {

    lateinit var viewModel: LoginViewModel
    lateinit var switcher: Switcher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        setupView()
        signIn(true)
    }

    private fun setupView() {
        setupSwitcher()
        setupErrorObserver()
        setupUserObserver()
        setupDismissErrorButton(getString(R.string.try_again))
    }

    override fun setupSwitcher() {
        switcher = Switcher.Builder(this)
                .addContentView(contentAtLoginActivity)
                .addProgressView(progressView)
                .addErrorView(errorView)
                .setErrorLabel(errorLabel)
                .build()
    }

    override fun setupDismissErrorButton(title: String) {
        dismissErrorButton.text = title
        dismissErrorButton.setOnClickListener { signIn(false) }
    }

    override fun setupErrorObserver() {
        viewModel.errorLiveData.observe(this, Observer { switcher.showErrorView() })
    }

    override fun setupUserObserver() {
        viewModel.userLiveData.observe(this, Observer { NavigationActivity.start(this) })
    }

    override fun signIn(firstAttempt: Boolean) {
        if (firstAttempt) switcher.showProgressViewImmediately() else switcher.showProgressView()
        viewModel.signInAnonymously()
    }


}