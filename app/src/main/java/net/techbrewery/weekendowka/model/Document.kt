package net.techbrewery.weekendowka.model

import android.os.Bundle
import net.techbrewery.weekendowka.base.BundleKey
import org.joda.time.DateTime
import java.io.Serializable
import java.util.*

/**
 * Created by Jacek Kwiecień on 13.10.2017.
 */
class Document(var id: String = UUID.randomUUID().toString()) : Serializable {


    var driverAction = ""
    val dateOfCreation = Date()
    var actionStartDate = DateTime.now().toDate()
    var actionEndDate = DateTime.now().toDate()
    var placeOfDeclarerSigning = ""
    var dateOfDeclarerSigning = DateTime.now().toDate()
    var placeOfDriverSigning = ""
    var dateOfDriverSigning = DateTime.now().toDate()
    var declarer: Declarer? = null
    var driver: Driver? = null

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

    fun copy(): Document {
        val bundle = Bundle()
        bundle.putSerializable(BundleKey.DOCUMENT, this)
        val copy = bundle.getSerializable(BundleKey.DOCUMENT) as Document
        copy.id = UUID.randomUUID().toString()
        return copy
    }
}