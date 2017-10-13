package net.techbrewery.weekendowka.base.extensions

import android.content.Context
import net.techbrewery.weekendowka.App
import net.techbrewery.weekendowka.base.network.Repository

/**
 * Created by Jacek Kwiecień on 13.10.2017.
 */


val Context.app: App get() = applicationContext as App

val Context.repository: Repository get() = app.repository