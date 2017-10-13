package net.techbrewery.weekendowka.model

import java.io.Serializable
import java.util.*

/**
 * Created by Jacek Kwiecień on 13.10.2017.
 */
data class Company(val id: String = UUID.randomUUID().toString()) : Serializable {
    var name = ""
    var address = ""
    var phone = ""
    var email = ""
}