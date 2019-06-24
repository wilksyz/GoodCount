package com.antoine.goodCount.ui.detail.viewpager.balance.recyclerView

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.antoine.goodCount.models.Participant
import kotlinx.android.synthetic.main.recyclerview_fragment_balance.view.*
import java.text.NumberFormat
import java.util.*

class BalanceFragmentRecyclerViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    fun updateUI(participant: Participant, balance: Double?, currency: String) {
        val format = NumberFormat.getCurrencyInstance(Locale.getDefault())
        format.currency = Currency.getInstance(currency)
        val balanceFormat = format.format(balance)
        val lineString = "${participant.username}: $balanceFormat"
        itemView.balance_recyclerview_textView.text = lineString
    }
}