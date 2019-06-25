package com.antoine.goodCount.ui.detail.viewpager.spent.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.antoine.goodCount.R
import com.antoine.goodCount.models.CommonPot
import com.antoine.goodCount.models.LineCommonPot
import com.antoine.goodCount.models.Participant

class SpentFragmentRecyclerViewAdapter(private val clickListener: SpentClickListener): RecyclerView.Adapter<SpentFragmentRecyclerViewHolder>(){

    private var mLineCommonPotList : List<LineCommonPot> = ArrayList()
    private var mCommonPot: CommonPot? = null
    private var mParticipantMap: HashMap<String, Participant> = HashMap()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpentFragmentRecyclerViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.recyclerview_activity_main, parent, false)
        return SpentFragmentRecyclerViewHolder(view, clickListener)
    }

    override fun getItemCount(): Int {
        return mLineCommonPotList.size
    }

    override fun onBindViewHolder(spentFragmentRecyclerViewHolder: SpentFragmentRecyclerViewHolder, position: Int) {
        return spentFragmentRecyclerViewHolder.updateLineCommonPotList(mLineCommonPotList[position], mCommonPot?.currency, mParticipantMap)
    }

    fun updateData(commonPotList: List<LineCommonPot>){
        mLineCommonPotList = commonPotList
        this.notifyDataSetChanged()
    }

    fun updateCommonPot(commonPot: CommonPot){
        mCommonPot = commonPot
        this.notifyDataSetChanged()
    }

    fun updateParticipantList(participantMap: HashMap<String, Participant>){
        mParticipantMap = participantMap
        this.notifyDataSetChanged()
    }
}