package net.techbrewery.weekendowka.people

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_drivers.*
import net.techbrewery.weekendowka.R
import net.techbrewery.weekendowka.base.BundleKey
import net.techbrewery.weekendowka.base.RequestCode
import net.techbrewery.weekendowka.base.view.BaseActivity
import net.techbrewery.weekendowka.base.view.DividerItemDecorator
import net.techbrewery.weekendowka.model.Company
import net.techbrewery.weekendowka.model.Driver
import net.techbrewery.weekendowka.onboarding.driver.DriverActivity

/**
 * Created by Jacek Kwiecień on 13.10.2017.
 */
class DriversActivity : BaseActivity() {

    companion object {
        fun start(activity: Activity, company: Company) {
            val intent = Intent(activity, DriversActivity::class.java)
            intent.putExtra(BundleKey.COMPANY, company)
            activity.startActivityForResult(intent, RequestCode.SELECT_DRIVER)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drivers)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val company = intent.getSerializableExtra(BundleKey.COMPANY) as Company
        val layoutManager = LinearLayoutManager(this)
        val divider = DividerItemDecorator(recyclerViewAtDriversActivity.context, layoutManager.orientation)

        recyclerViewAtDriversActivity.layoutManager = layoutManager
        recyclerViewAtDriversActivity.addItemDecoration(divider)
        recyclerViewAtDriversActivity.setHasFixedSize(true)
        recyclerViewAtDriversActivity.adapter = DriversAdapter(
                company.drivers,
                object : DriversAdapter.ClickListener {
                    override fun onClicked(driver: Driver) {
                        passSelectedDeclarer(driver)
                    }
                },
                object : AddNewViewHolder.ClickListener {
                    override fun onClicked() {
                        DriverActivity.startToAddNew(this@DriversActivity, company)
                    }
                })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == RequestCode.CREATE_DRIVER && data != null) {
            val driver = data.getSerializableExtra(BundleKey.DRIVER) as Driver
            passSelectedDeclarer(driver)
        }
    }

    private fun passSelectedDeclarer(driver: Driver) {
        val data = Intent()
        data.putExtra(BundleKey.DRIVER, driver)
        setResult(Activity.RESULT_OK, data)
        finish()
    }
}