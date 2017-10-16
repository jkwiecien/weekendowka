package net.techbrewery.weekendowka.onboarding.login

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.text.Html
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.view_error.*
import kotlinx.android.synthetic.main.view_progress.*
import net.techbrewery.weekendowka.R
import net.techbrewery.weekendowka.base.RequestCode
import net.techbrewery.weekendowka.base.view.BaseActivity
import net.techbrewery.weekendowka.document.DocumentActivity
import net.techbrewery.weekendowka.onboarding.company.CompanyActivity
import pl.aprilapps.switcher.Switcher
import timber.log.Timber


/**
 * Created by Jacek Kwiecień on 13.10.2017.
 */

class LoginActivity : BaseActivity(), LoginMvvm.View, GoogleApiClient.OnConnectionFailedListener {

    lateinit var viewModel: LoginMvvm.ViewModel
    lateinit var switcher: Switcher

    private val googleSignInOptions: GoogleSignInOptions by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
    }

    private val googleApiClient: GoogleApiClient by lazy {
        GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build()
    }

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
        startButtonAtOnboardingActivity.setSize(SignInButton.SIZE_WIDE)
        startButtonAtOnboardingActivity.setColorScheme(SignInButton.COLOR_DARK)
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

        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient)
        startActivityForResult(signInIntent, RequestCode.GOOGLE_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RequestCode.GOOGLE_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result.isSuccess) {
                Timber.i("Google sign in successfull")
                val account = result.signInAccount
                if (account != null) {
                    firebaseAuthWithGoogle(account)
                } else {
                    Timber.e("Google SignIn Account null")
                    switcher.showErrorView()
                }
            } else {
                Timber.e("Google sign in failed")
                switcher.showErrorView()
            }
        }
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        Timber.e(connectionResult.errorMessage)
        switcher.showErrorView()
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, OnCompleteListener<AuthResult> { task ->
                    if (task.isSuccessful) {
                        Timber.i("Firebase sign in successfull")
                        val user = FirebaseAuth.getInstance().currentUser
                        CompanyActivity.start(this, user?.email ?: "")
                        finish()
                    } else {
                        Timber.e("Firebase sign in failed")
                        switcher.showErrorView()
                    }
                })
    }


}