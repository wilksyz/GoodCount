package com.antoine.goodCount.ui.detail.viewpager.spent.recyclerView

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.antoine.goodCount.R
import com.antoine.goodCount.models.LineCommonPot
import com.antoine.goodCount.models.Participant
import kotlinx.android.synthetic.main.recyclerview_activity_main.view.*
import java.text.DateFormat
import java.text.NumberFormat
import java.util.*

private const val USER_APP = "user app"
class SpentFragmentRecyclerViewHolder(itemView: View, private var clickListener: SpentClickListener): RecyclerView.ViewHolder(itemView), View.OnClickListener {

    private lateinit var mLineCommonPot: LineCommonPot

    init {
        itemView.setOnClickListener(this)
    }

    fun updateLineCommonPotList(lineCommonPot: LineCommonPot, currency: String?, participantMap: HashMap<String, Participant>){
        mLineCommonPot = lineCommonPot
        itemView.main_view_holder_tittle_textView.text = lineCommonPot.title
        this.formatDate(lineCommonPot)
        this.formatCurrency(lineCommonPot, currency)
        this.formatPaidBy(lineCommonPot.paidBy, participantMap)
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

    private fun formatPaidBy(payerName: String, username: HashMap<String, Participant>){
        val paidBy: String = if (payerName == username[USER_APP]?.id){
            "${itemView.context.resources.getString(R.string.payed_by)} ${itemView.context.resources.getString(R.string.me)}"
        }else {
            "${itemView.context.resources.getString(R.string.payed_by)} ${username[payerName]?.username}"
        }
        itemView.main_view_holder_description_textView.text = paidBy
    }

    override fun onClick(v: View?) {
        clickListener.onClick(mLineCommonPot.id)
    }
}
interface SpentClickListener{
    fun onClick(lineCommonPotId: String)
}