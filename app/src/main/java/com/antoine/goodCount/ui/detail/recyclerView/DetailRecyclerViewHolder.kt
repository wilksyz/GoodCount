package com.antoine.goodCount.ui.detail.recyclerView

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.antoine.goodCount.R
import com.antoine.goodCount.models.LineCommonPot
import kotlinx.android.synthetic.main.recyclerview_activity_main.view.*
import java.text.DateFormat
import java.text.NumberFormat
import java.util.*

class DetailRecyclerViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    fun updateLineCommonPotList(lineCommonPot: LineCommonPot, currency: String?, username: String){
        itemView.main_view_holder_tittle_textView.text = lineCommonPot.title
        this.formatDate(lineCommonPot)
        this.formatCurrency(lineCommonPot, currency)
        this.formatPaidBy(lineCommonPot.paidBy, username)
    }

    private fun formatDate(lineCommonPot: LineCommonPot){
        val dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT, Locale.getDefault())
        val date = dateFormat.format(lineCommonPot.date)
        itemView.view_holder_date_textView.visibility = View.VISIBLE
        itemView.view_holder_date_textView.text = date
    }

    private fun formatCurrency(lineCommonPot: LineCommonPot, currency: String?){
        if (currency != null){
            val format = NumberFormat.getCurrencyInstance(Locale.getDefault())
            format.currency = Currency.getInstance(currency)
            val amount = format.format(lineCommonPot.amount)
            itemView.view_holder_price_textView.visibility = View.VISIBLE
            itemView.view_holder_price_textView.text = amount
        }
    }

    private fun formatPaidBy(payerName: String, username: String){
        val paidBy: String = if (payerName == username){
            "${itemView.context.resources.getString(R.string.payed_by)} ${itemView.context.resources.getString(R.string.me)}"
        }else {
            val split = payerName.split("\"")
            "${itemView.context.resources.getString(R.string.payed_by)} ${split[1]}"
        }
        itemView.main_view_holder_description_textView.text = paidBy
    }
}