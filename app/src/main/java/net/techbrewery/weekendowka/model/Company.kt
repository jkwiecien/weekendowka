package net.techbrewery.weekendowka.model

import java.io.Serializable

/**
 * Created by Jacek Kwiecień on 13.10.2017.
 */
data class Company(val id: String = "", val userId: String = "") : Serializable {
    var name = ""
    var address = ""
    var phone = ""
    var email = ""
    var drivers = mutableListOf<Driver>()
        private set
    var declarers = mutableListOf<Declarer>()
        private set
}