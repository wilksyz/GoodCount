package com.antoine.goodCount.ui.createAndEditSpent.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.antoine.goodCount.R
import com.antoine.goodCount.models.Participant

class CreateSpentRecyclerViewAdapter(private val clickListener: ClickListener): RecyclerView.Adapter<CreateSpentRecyclerViewHolder>() {

    private var mParticipantList: List<Participant> = ArrayList()
    private var mParticipantSelectedMap: Map<String, Boolean> = HashMap()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreateSpentRecyclerViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.recyclerview_activity_spent_create, parent, false)
        return CreateSpentRecyclerViewHolder(view, clickListener)
    }

    override fun getItemCount(): Int {
        return mParticipantList.size
    }

    override fun onBindViewHolder(createSpentRecyclerViewHolder: CreateSpentRecyclerViewHolder, position: Int) {
        return createSpentRecyclerViewHolder.updateParticipantList(mParticipantList[position], mParticipantSelectedMap[mParticipantList[position].id]
        )
    }

    fun updateData(participantList: List<Participant>, participantSelectedMap: Map<String, Boolean>){
        mParticipantList = participantList
        mParticipantSelectedMap = participantSelectedMap
        this.notifyDataSetChanged()
    }
}