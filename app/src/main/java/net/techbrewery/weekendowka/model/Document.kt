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
    val dateOfCreation = DateTime.now()
    var actionStartDate = DateTime.now().toDate()
    var actionEndDate = DateTime.now().toDate()
    var placeOfDeclarerSigning = ""
    var dateOfDeclarerSigning = DateTime.now().toDate()
    var placeOfDriverSigning = ""
    var dateOfDriverSigning = DateTime.now().toDate()

    fun setStartDate(date: DateTime) {
        var dateTime = DateTime(actionStartDate)
        dateTime = dateTime.withYear(date.year).withMonthOfYear(date.monthOfYear).withDayOfMonth(date.dayOfMonth)
        actionStartDate = dateTime.toDate()
    }

    fun setStartTime(time: Time) {
        var dateTime = DateTime(actionStartDate)
        dateTime = dateTime.withHourOfDay(time.hourOfDay).withMinuteOfHour(time.minuteOfHour)
        actionStartDate = dateTime.toDate()
    }

    fun setEndDate(date: DateTime) {
        var dateTime = DateTime(actionEndDate)
        dateTime = dateTime.withYear(date.year).withMonthOfYear(date.monthOfYear).withDayOfMonth(date.dayOfMonth)
        actionEndDate = dateTime.toDate()
    }

    fun setEndTime(time: Time) {
        var dateTime = DateTime(actionStartDate)
        dateTime = dateTime.withHourOfDay(time.hourOfDay).withMinuteOfHour(time.minuteOfHour)
        actionEndDate = dateTime.toDate()
    }
}