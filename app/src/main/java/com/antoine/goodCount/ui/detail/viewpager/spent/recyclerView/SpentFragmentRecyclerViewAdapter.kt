package com.antoine.goodCount.ui.detail.viewpager.spent.recyclerView

import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.antoine.goodCount.R
import com.antoine.goodCount.models.CommonPot
import com.antoine.goodCount.models.LineCommonPot
import com.antoine.goodCount.models.Participant
import kotlinx.android.synthetic.main.recyclerview_activity_main.view.*

private const val PENDING_REMOVAL_TIMEOUT = 3000
class SpentFragmentRecyclerViewAdapter(private val clickListener: SpentClickListener): RecyclerView.Adapter<SpentFragmentRecyclerViewHolder>(){

    private var mLineCommonPotList : MutableList<LineCommonPot> = ArrayList()
    private var mCommonPot: CommonPot? = null
    private var mParticipantMap: HashMap<String, Participant> = HashMap()
    private var mLineCommonPotPendingRemoval: MutableList<LineCommonPot> = ArrayList()
    private val mHandler = Handler()
    private var mPendingRunnable = java.util.HashMap<LineCommonPot, Runnable>()

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
        val lineCommonPot = mLineCommonPotList[position]
        spentFragmentRecyclerViewHolder.itemView.undo_button.setOnClickListener {
            val pendingRemovalRunnable = mPendingRunnable[lineCommonPot]
            mPendingRunnable.remove(lineCommonPot)
            if (pendingRemovalRunnable != null) mHandler.removeCallbacks(pendingRemovalRunnable)
            mLineCommonPotPendingRemoval.remove(lineCommonPot)
            // this will rebind the row in "normal" state
            notifyItemChanged(mLineCommonPotList.indexOf(lineCommonPot))
        }
        return if (mLineCommonPotPendingRemoval.contains(lineCommonPot)){
            spentFragmentRecyclerViewHolder.updateLineCommonPotList(mLineCommonPotList[position], mCommonPot?.currency, mParticipantMap, true)
        }else{
            spentFragmentRecyclerViewHolder.updateLineCommonPotList(mLineCommonPotList[position], mCommonPot?.currency, mParticipantMap, false)
        }
    }

    fun updateData(commonPotList: MutableList<LineCommonPot>){
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

    fun pendingRemoval(position: Int) {
        val lineCommonPot = mLineCommonPotList[position]
        if (!mLineCommonPotPendingRemoval.contains(lineCommonPot)) {
            mLineCommonPotPendingRemoval.add(lineCommonPot)
            // this will redraw row in "undo" state
            notifyItemChanged(position)
            // let's create, store and post a runnable to remove the item
            val pendingRemovalRunnable = Runnable {
                remove(mLineCommonPotList.indexOf(lineCommonPot))
            }
            mHandler.postDelayed(pendingRemovalRunnable, PENDING_REMOVAL_TIMEOUT.toLong())
            mPendingRunnable[lineCommonPot] = pendingRemovalRunnable
        }
    }

    fun isPendingRemoval(position: Int): Boolean {
        val lineCommonPot = mLineCommonPotList[position]
        return mLineCommonPotPendingRemoval.contains(lineCommonPot)
    }

    private fun remove(position: Int) {
        val lineCommonPot = mLineCommonPotList[position]
        if (mLineCommonPotPendingRemoval.contains(lineCommonPot)) {
            mLineCommonPotPendingRemoval.remove(lineCommonPot)
        }
        if (mLineCommonPotList.contains(lineCommonPot)) {
            clickListener.onUndoClick(lineCommonPot)
            notifyItemRemoved(position)
        }
    }
}