package com.antoine.goodCount.ui.main.recyclerview

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.antoine.goodCount.models.CommonPot
import kotlinx.android.synthetic.main.recyclerview_activity_main.view.*

class MainRecyclerViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    fun updateListOfCommonPots(commonPot: CommonPot){
        itemView.main_view_holder_tittle_textView.text = commonPot.tittle
        itemView.main_view_holder_description_textView.text = commonPot.description
    }
}