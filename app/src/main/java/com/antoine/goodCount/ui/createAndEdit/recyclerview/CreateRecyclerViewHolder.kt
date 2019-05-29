package com.antoine.goodCount.ui.createAndEdit.recyclerview

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recyclerview_activity_create.view.*

class CreateRecyclerViewHolder(itemView: View,private val clickListener: ClickListener): RecyclerView.ViewHolder(itemView), View.OnClickListener {

    init {
        itemView.create_recyclerview_clear_imageButton.setOnClickListener(this)
    }

    fun updateListContributor(contributor: String){
        itemView.create_view_holder_contributor_textView.text = contributor
    }

    override fun onClick(v: View?) {
        clickListener.onClick(adapterPosition)
    }
}
interface ClickListener{
    fun onClick(position: Int)
}