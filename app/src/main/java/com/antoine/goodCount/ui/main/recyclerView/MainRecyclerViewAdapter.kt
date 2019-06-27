package com.antoine.goodCount.ui.main.recyclerView

import android.os.Handler
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
    private var commonPotPendingRemoval: MutableList<CommonPot> = ArrayList()
    private val mHandler = Handler()
    private var mPendingRunnable = HashMap<CommonPot, Runnable>()

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
        val commonPot = mCommonPotList[position]
        mainRecyclerViewHolder.itemView.undo_button.setOnClickListener {
            val pendingRemovalRunnable = mPendingRunnable[commonPot]
            mPendingRunnable.remove(commonPot)
            if (pendingRemovalRunnable != null) mHandler.removeCallbacks(pendingRemovalRunnable)
            commonPotPendingRemoval.remove(commonPot)
            // this will rebind the row in "normal" state
            notifyItemChanged(mCommonPotList.indexOf(commonPot))
        }
        return if (commonPotPendingRemoval.contains(commonPot)){
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
        val commonPot = mCommonPotList[position]
        if (!commonPotPendingRemoval.contains(commonPot)) {
            commonPotPendingRemoval.add(commonPot)
            // this will redraw row in "undo" state
            notifyItemChanged(position)
            // let's create, store and post a runnable to remove the item
            val pendingRemovalRunnable = Runnable {
                remove(mCommonPotList.indexOf(commonPot))
            }
            mHandler.postDelayed(pendingRemovalRunnable, PENDING_REMOVAL_TIMEOUT.toLong())
            mPendingRunnable[commonPot] = pendingRemovalRunnable
        }
    }

    fun isPendingRemoval(position: Int): Boolean {
        val commonPot = mCommonPotList[position]
        return commonPotPendingRemoval.contains(commonPot)
    }

    private fun remove(position: Int) {
        val commonPot = mCommonPotList[position]
        if (commonPotPendingRemoval.contains(commonPot)) {
            commonPotPendingRemoval.remove(commonPot)
        }
        if (mCommonPotList.contains(commonPot)) {
            clickListener.onUndoClick(mCommonPotList[position])
            mCommonPotList.removeAt(position)
            notifyItemRemoved(position)
        }
    }
}