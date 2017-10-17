package net.techbrewery.weekendowka.people

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import kotlinx.android.synthetic.main.view_driver.view.*
import net.techbrewery.weekendowka.R
import net.techbrewery.weekendowka.base.extensions.toDateTime
import net.techbrewery.weekendowka.model.Driver
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

/**
 * Created by Jacek Kwiecień on 17.10.2017.
 */
class DriversAdapter(private val drivers: List<Driver>, private val declarerClickListener: ClickListener, private val addNewClickListener: AddNewViewHolder.ClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private val TYPE_DRIVER = 0
        private val TYPE_ADD_NEW = 1
    }

    private val dateFormat: DateTimeFormatter by lazy { DateTimeFormat.forPattern("dd.MM.yyyy") }

    interface ClickListener {
        fun onClicked(driver: Driver)
    }

    override fun getItemViewType(position: Int): Int = when (position) {
        in 0 until drivers.size -> TYPE_DRIVER
        else -> TYPE_ADD_NEW
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = when (viewType) {
        TYPE_DRIVER -> ViewHolder(parent)
        else -> AddNewViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                val driver = drivers[position]
                holder.bind(driver, declarerClickListener)
            }
            is AddNewViewHolder -> holder.bind(addNewClickListener)
        }
    }

    override fun getItemCount(): Int {
        return drivers.size + 1
    }

    inner class ViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_driver, parent, false)) {

        fun bind(driver: Driver, clickListener: ClickListener) {
            itemView.nameLabelAtDriverView.text = driver.name
            itemView.birthdayLabelAtDriverView.text = dateFormat.print(driver.birthday.toDateTime())
            itemView.setOnClickListener { clickListener.onClicked(driver) }
        }
    }
}