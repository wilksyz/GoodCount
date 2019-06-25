package com.antoine.goodCount.ui.detail.viewpager.balance.recyclerView

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.antoine.goodCount.R
import com.antoine.goodCount.models.Participant
import kotlinx.android.synthetic.main.recyclerview_fragment_balance.view.*
import java.text.NumberFormat
import java.util.*

class BalanceFragmentRecyclerViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    fun updateUI(participant: Participant, balance: Double?, currency: String) {
        val format = NumberFormat.getCurrencyInstance(Locale.getDefault())
        format.currency = Currency.getInstance(currency)
        val balanceFormat = format.format(balance)
        itemView.balance_username_recyclerview_textView.text = participant.username
        itemView.balance_amount_recyclerview_textView.text = balanceFormat
        if (balance != null) {
            if (balance < 0){
                itemView.container_balance_recycler_view.background = ContextCompat.getDrawable(itemView.context, R.drawable.custom_background_negative)
            }else {
                itemView.container_balance_recycler_view.background = ContextCompat.getDrawable(itemView.context, R.drawable.custom_background_positive)
            }
        }
    }
}