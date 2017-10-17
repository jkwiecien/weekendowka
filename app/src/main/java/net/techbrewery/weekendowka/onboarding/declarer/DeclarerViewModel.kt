package net.techbrewery.weekendowka.onboarding.declarer

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import net.techbrewery.weekendowka.base.extensions.repository
import net.techbrewery.weekendowka.base.network.FirestoreRequestListener
import net.techbrewery.weekendowka.model.Company
import net.techbrewery.weekendowka.model.Declarer

/**
 * Created by Jacek Kwiecień on 13.10.2017.
 */
class DeclarerViewModel(application: Application) : AndroidViewModel(application), DeclarerMvvm.ViewModel {

    override lateinit var company: Company
    override var createFirst: Boolean = true

    override val eventLiveData: MutableLiveData<DeclarerViewEvent> = MutableLiveData()

    private val repository = application.repository

    override fun saveDeclarer(name: String, position: String) {
        val declarer = Declarer(name, position)
        company.declarers.add(declarer)
        company.selectedDeclarerId = declarer.id

        if (createFirst) {
            repository.saveCompany(company, object : FirestoreRequestListener<Company> {
                override fun onSuccess(responseObject: Company) {
                    eventLiveData.postValue(DeclarerViewEvent.DeclarerSaved(responseObject, declarer))
                }

                override fun onFailure(error: Throwable) {
                    eventLiveData.postValue(DeclarerViewEvent.Error(error))
                }

            })
        } else {
            eventLiveData.postValue(DeclarerViewEvent.DeclarerCreated(declarer))
        }


    }

}