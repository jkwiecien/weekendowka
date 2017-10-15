package net.techbrewery.weekendowka.base.extensions

import org.joda.time.DateTime
import java.util.*

/**
 * Created by Jacek Kwiecień on 15.10.2017.
 */

fun Date.toDateTime(): DateTime {
    return DateTime(this)
}