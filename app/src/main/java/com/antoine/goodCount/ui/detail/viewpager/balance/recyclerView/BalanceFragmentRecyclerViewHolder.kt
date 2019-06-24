package com.antoine.goodCount.ui.detail.viewpager.balance.recyclerView

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.antoine.goodCount.models.Participant
import kotlinx.android.synthetic.main.recyclerview_fragment_balance.view.*

class BalanceFragmentRecyclerViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    fun updateUI(participant: Participant, balance: Double?) {
        val lineString = "${participant.username}: $balance"
        itemView.balance_recyclerview_textView.text = lineString
    }
}