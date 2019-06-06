package com.antoine.goodCount.ui.createSpent.recyclerView

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recyclerview_activity_spent_create.view.*

class CreateSpentRecyclerViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    fun updateParticipantList(username: String){
        itemView.create_view_holder_contributor_textView.text = username
    }
}