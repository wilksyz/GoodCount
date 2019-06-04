package com.antoine.goodCount.ui.main.recyclerview

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.antoine.goodCount.R
import com.antoine.goodCount.models.CommonPot

class MainRecyclerViewAdapter(private val clickListener: ClickListener): RecyclerView.Adapter<MainRecyclerViewHolder>() {

    private var mCommonPotList : List<CommonPot> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainRecyclerViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.recyclerview_activity_main, parent, false)
        return MainRecyclerViewHolder(view, clickListener)
    }

    override fun getItemCount(): Int {
        return mCommonPotList.size
    }

    override fun onBindViewHolder(mainRecyclerViewHolder: MainRecyclerViewHolder, position: Int) {
        return mainRecyclerViewHolder.updateListOfCommonPots(mCommonPotList[position])
    }

    fun updateData(commonPotList: List<CommonPot>){
        mCommonPotList = commonPotList
        this.notifyDataSetChanged()
    }

    fun getCommonPotId(position: Int): String? {
        return mCommonPotList[position].id
    }
}