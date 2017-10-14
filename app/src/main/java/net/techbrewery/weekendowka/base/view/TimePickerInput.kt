package net.techbrewery.weekendowka.base.view

import android.app.TimePickerDialog
import android.content.Context
import android.util.AttributeSet
import android.widget.EditText
import net.techbrewery.weekendowka.model.Time

/**
 * Created by Jacek Kwiecień on 14.10.2017.
 */
class TimePickerInput : EditText {

    interface TimePickerListener {
        fun provideTime(): Time
        fun onTimePicked(time: Time)
    }

    var timePickerListener: TimePickerListener? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        isFocusableInTouchMode = false
        isClickable = true

        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hourOfDay, minuteOfHour ->
            val pickedTime = Time(hourOfDay, minuteOfHour)
            setText(pickedTime.toString())
            timePickerListener?.onTimePicked(pickedTime)
        }

        setOnClickListener {
            val initialTime = timePickerListener?.provideTime() ?: Time()
            TimePickerDialog(context, timeSetListener, initialTime.hourOfDay, initialTime.minuteOfHour, true).show()
        }

        update(timePickerListener?.provideTime() ?: Time())
    }

    fun update(time: Time) {
        setText(time.toString())
    }
}