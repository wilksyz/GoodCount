package com.antoine.goodCount.ui.main.recyclerView

import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.antoine.goodCount.models.CommonPot
import kotlinx.android.synthetic.main.recyclerview_activity_main.view.*

class MainRecyclerViewHolder(itemView: View, private var clickListener: ClickListener): RecyclerView.ViewHolder(itemView), View.OnClickListener{

    private lateinit var mCommonPot: CommonPot

    init {
        itemView.setOnClickListener(this)
    }

    fun updateListOfCommonPots(commonPot: CommonPot, isPendingRemoval: Boolean){
        if (!isPendingRemoval){
            mCommonPot = commonPot
            itemView.main_view_holder_tittle_textView.visibility = View.VISIBLE
            itemView.main_view_holder_description_textView.visibility = View.VISIBLE
            itemView.main_view_holder_tittle_textView.text = commonPot.title
            itemView.main_view_holder_description_textView.text = commonPot.description
            itemView.setBackgroundColor(Color.WHITE)
            itemView.container_main_view_holder.visibility = View.VISIBLE
            itemView.undo_button.visibility = View.INVISIBLE
        }else{
            itemView.main_view_holder_tittle_textView.visibility = View.INVISIBLE
            itemView.main_view_holder_description_textView.visibility = View.INVISIBLE
            itemView.setBackgroundColor(Color.RED)
            itemView.undo_button.visibility = View.VISIBLE
        }
    }

    override fun onClick(v: View) {
        clickListener.onClick(adapterPosition)
    }
}

interface ClickListener{
    fun onClick(position: Int)
    fun onDeleteSwipe(commonPot: CommonPot)
}