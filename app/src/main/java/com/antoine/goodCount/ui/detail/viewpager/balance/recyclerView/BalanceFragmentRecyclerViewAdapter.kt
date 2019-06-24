package com.antoine.goodCount.ui.detail.viewpager.balance.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.antoine.goodCount.R
import com.antoine.goodCount.models.Participant

class BalanceFragmentRecyclerViewAdapter: RecyclerView.Adapter<BalanceFragmentRecyclerViewHolder>() {

    private var mParticipantSpentMap: HashMap<String, Double> = HashMap()
    private var mParticipantList = ArrayList<Participant>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BalanceFragmentRecyclerViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.recyclerview_fragment_balance, parent, false)
        return BalanceFragmentRecyclerViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mParticipantList.size
    }

    override fun onBindViewHolder(balanceFragmentRecyclerViewHolder: BalanceFragmentRecyclerViewHolder, position: Int) {
        return balanceFragmentRecyclerViewHolder.updateUI(mParticipantList[position], mParticipantSpentMap[mParticipantList[position].id])
    }

    fun updateData(participantSpentMap: HashMap<String, Double>, participantList: ArrayList<Participant>){
        mParticipantSpentMap = participantSpentMap
        mParticipantList = participantList
        this.notifyDataSetChanged()
    }
}