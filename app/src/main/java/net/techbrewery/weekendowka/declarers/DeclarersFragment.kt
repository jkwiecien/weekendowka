package net.techbrewery.weekendowka.declarers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.techbrewery.weekendowka.R
import net.techbrewery.weekendowka.base.view.BaseFragment

/**
 * Created by Jacek Kwiecień on 13.10.2017.
 */
class DeclarersFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater?.inflate(R.layout.fragment_declarers, container, false)
    }
}