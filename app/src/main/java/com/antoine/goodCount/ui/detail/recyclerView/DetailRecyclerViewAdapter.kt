package com.antoine.goodCount.ui.detail.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.antoine.goodCount.R
import com.antoine.goodCount.models.CommonPot
import com.antoine.goodCount.models.LineCommonPot

class DetailRecyclerViewAdapter(): RecyclerView.Adapter<DetailRecyclerViewHolder>(){

    private var mLineCommonPotList : List<LineCommonPot> = ArrayList()
    private var mCommonPot: CommonPot? = null
    private var mUsername: String = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailRecyclerViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.recyclerview_activity_main, parent, false)
        return DetailRecyclerViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mLineCommonPotList.size
    }

    override fun onBindViewHolder(detailRecyclerViewHolder: DetailRecyclerViewHolder, position: Int) {
        return detailRecyclerViewHolder.updateLineCommonPotList(mLineCommonPotList[position], mCommonPot?.currency, mUsername)
    }

    fun updateData(commonPotList: List<LineCommonPot>){
        mLineCommonPotList = commonPotList
        this.notifyDataSetChanged()
    }

    fun updateCommonPot(commonPot: CommonPot){
        mCommonPot = commonPot
        this.notifyDataSetChanged()
    }

    fun updateUsername(username: String){
        mUsername = username
        this.notifyDataSetChanged()
    }

    fun getCommonPotId(position: Int): String? {
        return mLineCommonPotList[position].id
    }
}