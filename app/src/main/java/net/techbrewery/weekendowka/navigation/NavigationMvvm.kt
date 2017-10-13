package net.techbrewery.weekendowka.navigation

/**
 * Created by Jacek Kwiecień on 13.10.2017.
 */
interface NavigationMvvm {

    interface View {
        fun addDeclarersFragment()
        fun addDriversFragment()
        fun addDocumentFragment()
    }

    interface ViewModel {

    }
}