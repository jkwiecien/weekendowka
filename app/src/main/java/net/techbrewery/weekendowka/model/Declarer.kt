package net.techbrewery.weekendowka.model

import java.io.Serializable
import java.util.*

/**
 * Created by Jacek Kwiecień on 13.10.2017.
 */
class Declarer(val id: String = UUID.randomUUID().toString()) : Serializable {
    var name = ""
    var position = ""

    constructor(name: String, position: String) : this(UUID.randomUUID().toString()) {
        this.name = name
        this.position = position
    }
}