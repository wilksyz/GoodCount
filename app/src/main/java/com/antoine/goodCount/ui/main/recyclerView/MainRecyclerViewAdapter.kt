package com.antoine.goodCount.ui.main.recyclerView

import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.antoine.goodCount.R
import com.antoine.goodCount.models.CommonPot
import kotlinx.android.synthetic.main.recyclerview_activity_main.view.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.MutableList
import kotlin.collections.set

private const val PENDING_REMOVAL_TIMEOUT = 3000
class MainRecyclerViewAdapter(private val clickListener: ClickListener): RecyclerView.Adapter<MainRecyclerViewHolder>() {

    private var mCommonPotList : MutableList<CommonPot> = ArrayList()
    private var itemsPendingRemoval: MutableList<CommonPot> = ArrayList()
    private val handler = Handler()
    private var pendingRunnables = HashMap<CommonPot, Runnable>()

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
        val item = mCommonPotList[position]
        mainRecyclerViewHolder.itemView.undo_button.setOnClickListener {
            val pendingRemovalRunnable = pendingRunnables[item]
            pendingRunnables.remove(item)
            if (pendingRemovalRunnable != null) handler.removeCallbacks(pendingRemovalRunnable)
            itemsPendingRemoval.remove(item)
            // this will rebind the row in "normal" state
            notifyItemChanged(mCommonPotList.indexOf(item))
        }
        return if (itemsPendingRemoval.contains(item)){
            mainRecyclerViewHolder.updateListOfCommonPots(mCommonPotList[position], true)
        }else {
            mainRecyclerViewHolder.updateListOfCommonPots(mCommonPotList[position], false)
        }
    }

    fun updateData(commonPotList: MutableList<CommonPot>){
        mCommonPotList = commonPotList
        this.notifyDataSetChanged()
    }

    fun getCommonPotId(position: Int): String? {
        return mCommonPotList[position].id
    }

    fun getCommonPot(position: Int): CommonPot {
        return mCommonPotList[position]
    }

    fun pendingRemoval(position: Int) {
        val item = mCommonPotList[position]
        Log.e("TAG","Passage pending removal")
        if (!itemsPendingRemoval.contains(item)) {
            itemsPendingRemoval.add(item)
            // this will redraw row in "undo" state
            notifyItemChanged(position)
            // let's create, store and post a runnable to remove the item
            val pendingRemovalRunnable = Runnable {
                remove(mCommonPotList.indexOf(item))
                Log.e("TAG","3 secondes")
            }
            handler.postDelayed(pendingRemovalRunnable, PENDING_REMOVAL_TIMEOUT.toLong())
            pendingRunnables[item] = pendingRemovalRunnable
        }
    }

    fun isPendingRemoval(position: Int): Boolean {
        Log.e("TAG", "Passage is pending removal")
        val item = mCommonPotList[position]
        return itemsPendingRemoval.contains(item)
    }

    private fun remove(position: Int) {
        val item = mCommonPotList[position]
        if (itemsPendingRemoval.contains(item)) {
            itemsPendingRemoval.remove(item)
        }
        if (mCommonPotList.contains(item)) {
            clickListener.onLongClick(mCommonPotList[position])
            mCommonPotList.removeAt(position)
            notifyItemRemoved(position)
        }
    }
}