package net.techbrewery.weekendowka.base

/**
 * Created by Jacek Kwiecień on 13.10.2017.
 */

class Configuration {
    companion object {
        val DEFAULT_DRIVER_AGE = 30
        val DEFAULT_EMPLOYMENT_TIME = 1
    }
}

class Collection {
    companion object {
        val COMPANIES = "companies"
        val DECLARERS = "declarers"
        val DRIVERS = "drivers"
        val DOCUMENTS = "documents"
    }
}

class BundleKey {
    companion object {
        val CREATE_FIRST = "net.techbrewery.weekendowka_CREATE_FIRST"
        val COMPANY = "net.techbrewery.weekendowka_COMPANY"
        val DOCUMENT = "net.techbrewery.weekendowka_DOCUMENT"
        val EMAIL = "net.techbrewery.weekendowka_EMAIL"
        val DECLARER = "net.techbrewery.weekendowka_DECLARER"
        val DRIVER = "net.techbrewery.weekendowka_DRIVER"
        val PLACE_OF_DECLARER_SIGNING = "net.techbrewery.weekendowka_PLACE_OF_DECLARER_SIGNING"
        val PLACE_OF_DRIVER_SIGNING = "net.techbrewery.weekendowka_PLACE_OF_DRIVER_SIGNING"
    }
}

class RequestCode {
    companion object {
        val GOOGLE_SIGN_IN = 101
        val SELECT_DECLARER = 102
        val CREATE_DECLARER = 103
        val SELECT_DRIVER = 104
        val CREATE_DRIVER = 105
        val REQUEST_WRITE_PERMISSION = 106
    }
}