package com.antoine.goodCount.ui.detail.recyclerView

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.antoine.goodCount.R
import com.antoine.goodCount.models.LineCommonPot
import kotlinx.android.synthetic.main.recyclerview_activity_main.view.*
import java.text.DateFormat
import java.util.*
import java.text.NumberFormat


class DetailRecyclerViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    fun updateLineCommonPotList(lineCommonPot: LineCommonPot, currency: String?){
        itemView.main_view_holder_tittle_textView.text = lineCommonPot.title
        val payedBy = "${itemView.context.resources.getString(R.string.payed_by)} ${lineCommonPot.paidBy}"
        itemView.main_view_holder_description_textView.text = payedBy
        this.formatDate(lineCommonPot)
        this.formatCurrency(lineCommonPot, currency)
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
            val result = format.format(lineCommonPot.amount)
            itemView.view_holder_price_textView.visibility = View.VISIBLE
            itemView.view_holder_price_textView.text = result
        }
    }
}