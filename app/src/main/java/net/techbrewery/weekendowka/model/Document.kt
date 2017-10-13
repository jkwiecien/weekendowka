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
    var actionStartTime = DateTime.now()
    var actionEndTime = DateTime.now()
    var placeOfDeclarerSigning = ""
    var dateOfDeclarerSigning = DateTime.now()
    var placeOfDriverSigning = ""
    var dateOfDriverSigning = DateTime.now()
}