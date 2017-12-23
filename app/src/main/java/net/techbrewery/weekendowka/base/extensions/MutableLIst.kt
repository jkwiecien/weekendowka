package net.techbrewery.weekendowka.base.extensions

import android.util.Log

/**
 * Created by Jacek Kwiecień on 17.10.2017.
 */

fun <T> MutableList<T>.addOrReplaceFirst(item: T) {
    var position = -1

    forEachIndexed { index, iteratedItem ->
        Log.i("jacek", "item: $iteratedItem")
        if (item == iteratedItem) {
            position = index
            return@forEachIndexed
        }
    }

    if (position >= 0) {
        removeAt(position)
        add(position, item)
    } else {
        add(item)
    }
}