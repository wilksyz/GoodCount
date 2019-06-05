package com.antoine.goodCount.ui.main.recyclerView

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.antoine.goodCount.models.CommonPot
import kotlinx.android.synthetic.main.recyclerview_activity_main.view.*

class MainRecyclerViewHolder(itemView: View, private var clickListener: ClickListener): RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener{

    init {
        itemView.setOnClickListener(this)
        itemView.setOnLongClickListener(this)
    }

    fun updateListOfCommonPots(commonPot: CommonPot){
        itemView.main_view_holder_tittle_textView.text = commonPot.title
        itemView.main_view_holder_description_textView.text = commonPot.description
    }

    override fun onClick(v: View) {
        clickListener.onClick(adapterPosition)
    }

    override fun onLongClick(v: View): Boolean {
        return clickListener.onLongClick(adapterPosition)
    }
}

interface ClickListener{
    fun onClick(position: Int)
    fun onLongClick(position: Int): Boolean
}