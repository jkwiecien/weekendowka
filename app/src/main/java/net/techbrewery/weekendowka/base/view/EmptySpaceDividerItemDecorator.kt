package net.techbrewery.weekendowka.base.view

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Created by Jacek Kwiecień on 17.10.2017.
 */
class EmptySpaceDividerItemDecorator(private val dividerSizePx: Int, private val edges: List<Edge>) : RecyclerView.ItemDecoration() {

    var skipFirst = false
    var skipLast = false

    enum class Edge { LEFT, RIGHT, TOP, BOTTOM }

    override fun getItemOffsets(outRect: Rect, view: View?, parent: RecyclerView, state: RecyclerView.State?) {

        if (skipFirst && parent.getChildAdapterPosition(view) == 0) return
        if (skipLast && parent.getChildAdapterPosition(view) == parent.adapter.itemCount - 1) return

        if (edges.contains(Edge.LEFT)) outRect.left = dividerSizePx
        if (edges.contains(Edge.RIGHT)) outRect.right = dividerSizePx
        if (edges.contains(Edge.TOP)) outRect.top = dividerSizePx
        if (edges.contains(Edge.BOTTOM)) outRect.bottom = dividerSizePx


    }
}