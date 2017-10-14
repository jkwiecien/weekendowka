package net.techbrewery.weekendowka.model

import net.techbrewery.weekendowka.base.Configuration
import org.joda.time.DateTime
import java.io.Serializable
import java.util.*

/**
 * Created by Jacek Kwiecień on 13.10.2017.
 */
class Driver(val id: String = UUID.randomUUID().toString()) : Serializable {
    var name = ""
    var birthday: DateTime = DateTime.now().minusYears(Configuration.DEFAULT_DRIVER_AGE)
    var idNumber = ""
    var employmentDate: DateTime = DateTime.now().minusYears(Configuration.DEFAULT_EMPLOYMENT_TIME)
}