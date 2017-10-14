package net.techbrewery.weekendowka.model

import org.joda.time.DateTime
import java.io.Serializable
import java.util.*

/**
 * Created by Jacek Kwiecień on 13.10.2017.
 */
class Document(val id: String = UUID.randomUUID().toString()) : Serializable {

    enum class DriverAction {
        L4, VACATION, REST, OTHER_DRIVING, OTHER_WORK, STANDBY
    }

    var action = DriverAction.REST
    var actionStartDateTime = DateTime.now()
    var actionEndDateTime = DateTime.now()
    var placeOfDeclarerSigning = ""
    var dateOfDeclarerSigning = DateTime.now()
    var placeOfDriverSigning = ""
    var dateOfDriverSigning = DateTime.now()

    fun setStartDate(date: DateTime) {
        actionStartDateTime = actionStartDateTime.withYear(date.year).withMonthOfYear(date.monthOfYear).withDayOfMonth(date.dayOfMonth)
    }

    fun setStartTime(time: Time) {
        actionStartDateTime = actionStartDateTime.withHourOfDay(time.hourOfDay).withMinuteOfHour(time.minuteOfHour)
    }

    fun setEndDate(date: DateTime) {
        actionEndDateTime = actionEndDateTime.withYear(date.year).withMonthOfYear(date.monthOfYear).withDayOfMonth(date.dayOfMonth)
    }

    fun setEndTime(time: Time) {
        actionEndDateTime = actionEndDateTime.withHourOfDay(time.hourOfDay).withMinuteOfHour(time.minuteOfHour)
    }
}