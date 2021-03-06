package net.techbrewery.weekendowka.navigation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import net.techbrewery.weekendowka.R
import net.techbrewery.weekendowka.base.BundleKey
import net.techbrewery.weekendowka.base.view.BaseActivity
import net.techbrewery.weekendowka.model.Company

class NavigationActivity : BaseActivity(), NavigationMvvm.View {

    companion object {
        fun start(activity: Activity, company: Company) {
            val intent = Intent(activity, NavigationActivity::class.java)
            intent.putExtra(BundleKey.COMPANY, company)
            activity.startActivity(intent)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)
        setupView()
    }

    override fun addDeclarersFragment() {
        //TODO
    }

    override fun addDriversFragment() {
        //TODO
    }

    override fun addDocumentFragment() {
        //TODO
    }

    private fun setupView() {
        addDeclarersFragment()
        addDriversFragment()
        addDocumentFragment()
    }
}
