package net.techbrewery.weekendowka

import android.app.Application
import com.google.firebase.FirebaseApp
import net.techbrewery.weekendowka.base.network.Repository
import net.techbrewery.weekendowka.base.network.WeekendowkaRepository

/**
 * Created by Jacek Kwiecień on 13.10.2017.
 */
class App : Application() {

    val repository: Repository = WeekendowkaRepository()

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}