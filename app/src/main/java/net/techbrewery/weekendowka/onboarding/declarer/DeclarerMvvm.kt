package net.techbrewery.weekendowka.onboarding.declarer

import android.arch.lifecycle.MutableLiveData
import net.techbrewery.weekendowka.model.Company

/**
 * Created by Jacek Kwiecień on 13.10.2017.
 */
interface DeclarerMvvm {

    interface View {
        fun setupView()
        fun setupSwitcher()
        fun setupSaveButton()
        fun setupDismissErrorButton()
        fun setupEventObserver()
        fun setupNameInput()
        fun setupPositionInput()
    }

    interface ViewModel {
        var company: Company
        var createFirst: Boolean
        val eventLiveData: MutableLiveData<DeclarerViewEvent>

        fun saveDeclarer(name: String, position: String)
    }
}