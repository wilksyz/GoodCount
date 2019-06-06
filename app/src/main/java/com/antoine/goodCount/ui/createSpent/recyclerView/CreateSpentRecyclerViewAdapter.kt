package com.antoine.goodCount.ui.createSpent.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.antoine.goodCount.R

class CreateSpentRecyclerViewAdapter: RecyclerView.Adapter<CreateSpentRecyclerViewHolder>() {

    private var mParticipantName: List<String> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreateSpentRecyclerViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.recyclerview_activity_spent_create, parent, false)
        return CreateSpentRecyclerViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mParticipantName.size
    }

    override fun onBindViewHolder(createSpentRecyclerViewHolder: CreateSpentRecyclerViewHolder, position: Int) {
        return createSpentRecyclerViewHolder.updateParticipantList(mParticipantName[position])
    }

    fun updateData(commonPotList: List<String>){
        mParticipantName = commonPotList
        this.notifyDataSetChanged()
    }
}