package net.techbrewery.weekendowka

import android.app.Application
import com.google.firebase.FirebaseApp
import net.techbrewery.weekendowka.base.network.Repository
import net.techbrewery.weekendowka.base.network.WeekendowkaRepository
import timber.log.Timber

/**
 * Created by Jacek Kwiecień on 13.10.2017.
 */
class App : Application() {

    lateinit var repository: Repository

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
        repository = WeekendowkaRepository(applicationContext)
        FirebaseApp.initializeApp(this)
    }
}