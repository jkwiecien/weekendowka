package net.techbrewery.weekendowka.model

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

/**
 * Created by Jacek Kwiecień on 14.10.2017.
 */
class Time(var hourOfDay: Int = 0, var minuteOfHour: Int = 0) {

    constructor(dateTime: DateTime) : this(dateTime.hourOfDay, dateTime.minuteOfHour)

    override fun toString(): String {
        val dateTimeFormat = DateTimeFormat.forPattern("HH:mm")
        val dateTime = DateTime.now().withHourOfDay(hourOfDay).withMinuteOfHour(minuteOfHour)
        return dateTimeFormat.print(dateTime)
    }
}