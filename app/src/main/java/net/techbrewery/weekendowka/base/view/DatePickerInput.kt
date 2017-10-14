package net.techbrewery.weekendowka.base.view

import android.app.DatePickerDialog
import android.content.Context
import android.util.AttributeSet
import android.widget.EditText
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

/**
 * Created by Jacek Kwiecień on 14.10.2017.
 */
class DatePickerInput : EditText {

    interface DatePickerListener {
        fun provideDate(): DateTime
        fun onDatePicked(date: DateTime)
    }

    var datePickerListener: DatePickerListener? = null

    private val dateTimeFormat = DateTimeFormat.forPattern("dd.MM.yyyy")

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

        val dateSetListener = DatePickerDialog.OnDateSetListener { pickerView, year, month, day ->
            val pickedDate = DateTime(year, month + 1, day, 6, 0)
            setText(dateTimeFormat.print(pickedDate))
            datePickerListener?.onDatePicked(pickedDate)
        }

        setOnClickListener {
            val initialDate = datePickerListener?.provideDate() ?: DateTime.now()
            DatePickerDialog(context, dateSetListener, initialDate.year, initialDate.monthOfYear - 1, initialDate.dayOfMonth).show()
        }

        update(datePickerListener?.provideDate() ?: DateTime.now())
    }

    fun update(date: DateTime) {
        setText(dateTimeFormat.print(date))
    }
}