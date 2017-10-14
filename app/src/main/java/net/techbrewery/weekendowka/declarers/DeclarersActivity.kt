package net.techbrewery.weekendowka.declarers

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import net.techbrewery.weekendowka.R
import net.techbrewery.weekendowka.base.BundleKey
import net.techbrewery.weekendowka.base.view.BaseActivity
import net.techbrewery.weekendowka.model.Company

/**
 * Created by Jacek Kwiecień on 13.10.2017.
 */
class DeclarersActivity : BaseActivity() {

    companion object {
        fun start(activity: Activity, company: Company) {
            val intent = Intent(activity, DeclarersActivity::class.java)
            intent.putExtra(BundleKey.COMPANY, company)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_declarers)
    }

}