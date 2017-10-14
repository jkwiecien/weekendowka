package net.techbrewery.weekendowka.model

import java.io.Serializable
import java.util.*

/**
 * Created by Jacek Kwiecień on 13.10.2017.
 */
data class Company(var id: String = UUID.randomUUID().toString(), val userId: String = "") : Serializable {
    var name = ""
    var address = ""
    var phone = ""
    var email = ""
    var drivers = mutableListOf<Driver>()
        private set
    var declarers = mutableListOf<Declarer>()
        private set
    var selectedDeclarerId: String? = null
    var selectedDriverId: String? = null

    constructor(userId: String, name: String, address: String, phone: String, email: String) : this(UUID.randomUUID().toString(), userId) {
        this.name = name
        this.address = address
        this.phone = phone
        this.email = email
    }

    fun getSelectedDeclarer(): Declarer? {
        return declarers.find { it.id == selectedDeclarerId }
    }

    fun getSelectedDriver(): Driver? {
        return drivers.find { it.id == selectedDriverId }
    }
}