package net.techbrewery.weekendowka.people

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import net.techbrewery.weekendowka.R

/**
 * Created by Jacek Kwiecień on 17.10.2017.
 */
class AddNewViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_add_new, parent, false)) {

    interface ClickListener {
        fun onClicked()
    }

    fun bind(clickListener: ClickListener) {
        itemView.setOnClickListener { clickListener.onClicked() }
    }
}