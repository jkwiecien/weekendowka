package net.techbrewery.weekendowka.base

import android.text.Editable
import android.text.TextWatcher

/**
 * Created by Jacek Kwiecień on 17.10.2017.
 */
abstract class TextChangedListener : TextWatcher {

    override fun afterTextChanged(p0: Editable?) {}

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun onTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
        onTextChanged(charSequence.toString())
    }

    abstract fun onTextChanged(newText: String)
}