package net.techbrewery.weekendowka.onboarding.driver

import net.techbrewery.weekendowka.model.Company

/**
 * Created by Jacek Kwiecień on 14.10.2017.
 */
sealed class DriverViewEvent {
    class Error(val error: Throwable) : DriverViewEvent()
    class DriverSaved(val company: Company) : DriverViewEvent()
}