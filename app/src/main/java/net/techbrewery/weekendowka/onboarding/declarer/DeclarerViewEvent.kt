package net.techbrewery.weekendowka.onboarding.declarer

import net.techbrewery.weekendowka.model.Company

/**
 * Created by Jacek Kwiecień on 14.10.2017.
 */
sealed class DeclarerViewEvent {
    class Error(val error: Throwable) : DeclarerViewEvent()
    class DeclarerSaved(val company: Company) : DeclarerViewEvent()
}