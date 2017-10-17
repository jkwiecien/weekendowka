package net.techbrewery.weekendowka.people

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_declarers.*
import net.techbrewery.weekendowka.R
import net.techbrewery.weekendowka.base.BundleKey
import net.techbrewery.weekendowka.base.RequestCode
import net.techbrewery.weekendowka.base.view.BaseActivity
import net.techbrewery.weekendowka.base.view.DividerItemDecorator
import net.techbrewery.weekendowka.model.Company
import net.techbrewery.weekendowka.model.Declarer
import net.techbrewery.weekendowka.onboarding.declarer.DeclarerActivity

/**
 * Created by Jacek Kwiecień on 13.10.2017.
 */
class DeclarersActivity : BaseActivity() {

    companion object {
        fun start(activity: Activity, company: Company) {
            val intent = Intent(activity, DeclarersActivity::class.java)
            intent.putExtra(BundleKey.COMPANY, company)
            activity.startActivityForResult(intent, RequestCode.SELECT_DECLARER)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_declarers)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val company = intent.getSerializableExtra(BundleKey.COMPANY) as Company
        val layoutManager = LinearLayoutManager(this)
        val divider = DividerItemDecorator(recyclerViewAtDeclarersActivity.context, layoutManager.orientation)

        recyclerViewAtDeclarersActivity.layoutManager = layoutManager
        recyclerViewAtDeclarersActivity.addItemDecoration(divider)
        recyclerViewAtDeclarersActivity.setHasFixedSize(true)
        recyclerViewAtDeclarersActivity.adapter = DeclarersAdapter(
                company.declarers,
                object : DeclarersAdapter.ClickListener {
                    override fun onClicked(declarer: Declarer) {
                        passSelectedDeclarer(declarer)
                    }
                },
                object : AddNewViewHolder.ClickListener {
                    override fun onClicked() {
                        DeclarerActivity.startToAddNew(this@DeclarersActivity, company)
                    }
                })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == RequestCode.CREATE_DECLARER && data != null) {
            val declarer = data.getSerializableExtra(BundleKey.DECLARER) as Declarer
            passSelectedDeclarer(declarer)
        }
    }

    private fun passSelectedDeclarer(declarer: Declarer) {
        val data = Intent()
        data.putExtra(BundleKey.DECLARER, declarer)
        setResult(Activity.RESULT_OK, data)
        finish()
    }
}