package net.techbrewery.weekendowka.onboarding.driver

import net.techbrewery.weekendowka.model.Company
import net.techbrewery.weekendowka.model.Driver

/**
 * Created by Jacek Kwiecień on 14.10.2017.
 */
sealed class DriverViewEvent {
    class Error(val error: Throwable) : DriverViewEvent()
    class DriverSaved(val company: Company) : DriverViewEvent()
    class DriverCreated(val driver: Driver) : DriverViewEvent()
}