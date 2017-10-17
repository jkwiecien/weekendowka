package net.techbrewery.weekendowka.people

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import kotlinx.android.synthetic.main.view_declarer.view.*
import net.techbrewery.weekendowka.R
import net.techbrewery.weekendowka.model.Declarer

/**
 * Created by Jacek Kwiecień on 17.10.2017.
 */
class DeclarersAdapter(private val declarers: List<Declarer>, private val declarerClickListener: ClickListener, private val addNewClickListener: AddNewViewHolder.ClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private val TYPE_DECLARER = 0
        private val TYPE_ADD_NEW = 1
    }

    interface ClickListener {
        fun onClicked(declarer: Declarer)
    }

    override fun getItemViewType(position: Int): Int = when (position) {
        in 0 until declarers.size -> TYPE_DECLARER
        else -> TYPE_ADD_NEW
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = when (viewType) {
        TYPE_DECLARER -> ViewHolder(parent)
        else -> AddNewViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                val declarer = declarers[position]
                holder.bind(declarer, declarerClickListener)
            }
            is AddNewViewHolder -> holder.bind(addNewClickListener)
        }
    }

    override fun getItemCount(): Int {
        return declarers.size + 1
    }

    inner class ViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_declarer, parent, false)) {

        fun bind(declarer: Declarer, clickListener: ClickListener) {
            itemView.nameLabelAtDeclarerView.text = declarer.name
            itemView.positionLabelAtDeclarerView.text = declarer.position
            itemView.setOnClickListener { clickListener.onClicked(declarer) }
        }
    }
}

