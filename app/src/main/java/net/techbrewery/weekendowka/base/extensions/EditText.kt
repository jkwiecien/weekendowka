package net.techbrewery.weekendowka.base.extensions

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText


/**
 * Created by Jacek Kwiecień on 17.10.2017.
 */
fun EditText.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun EditText.hideKeyboardDelayed(delayMillis: Long) {
    postDelayed({ hideKeyboard() }, delayMillis)
}