package com.antoine.goodCount.ui.createAndEdit.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.antoine.goodCount.R

class CreateRecyclerViewAdapter(private val clickListener: ClickListener): RecyclerView.Adapter<CreateRecyclerViewHolder>() {

    private var mContributorList : List<String> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreateRecyclerViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.recyclerview_activity_create, parent, false)
        return CreateRecyclerViewHolder(view, clickListener)
    }

    override fun getItemCount(): Int {
        return mContributorList.size
    }

    override fun onBindViewHolder(createRecyclerViewHolder: CreateRecyclerViewHolder, position: Int) {
        return createRecyclerViewHolder.updateListContributor(mContributorList[position])
    }

    fun updateData(contributorList: List<String>){
        mContributorList = contributorList
        this.notifyDataSetChanged()
    }
}