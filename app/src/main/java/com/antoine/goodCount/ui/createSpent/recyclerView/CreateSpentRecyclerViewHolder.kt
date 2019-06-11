package com.antoine.goodCount.ui.createSpent.recyclerView

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.antoine.goodCount.models.Participant
import kotlinx.android.synthetic.main.recyclerview_activity_spent_create.view.*

class CreateSpentRecyclerViewHolder(itemView: View, private var clickListener: ClickListener): RecyclerView.ViewHolder(itemView), View.OnClickListener {

    private lateinit var mParticipant: Participant

    init {
        itemView.create_spent_view_holder_checkBox.setOnClickListener(this)
    }

    fun updateParticipantList(participant: Participant, isSelected: Boolean?){
        mParticipant = participant
        itemView.create_spent_view_holder_contributor_textView.text = participant.username
        if (isSelected != null) itemView.create_spent_view_holder_checkBox.isChecked = isSelected
    }

    override fun onClick(v: View) {
        clickListener.onClick(itemView.create_spent_view_holder_checkBox.isChecked, mParticipant.id)
    }

}
interface ClickListener{
    fun onClick(isChecked: Boolean, participantId: String)
}