package com.antoine.goodCount.ui.detail.viewpager.spent

import android.app.Activity
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.antoine.goodCount.R
import com.antoine.goodCount.models.CommonPot
import com.antoine.goodCount.models.LineCommonPot
import com.antoine.goodCount.ui.createAndEditSpent.create.CreateSpentActivity
import com.antoine.goodCount.ui.createAndEditSpent.edit.EditSpentActivity
import com.antoine.goodCount.ui.detail.viewpager.spent.recyclerView.SpentClickListener
import com.antoine.goodCount.ui.detail.viewpager.spent.recyclerView.SpentFragmentRecyclerViewAdapter
import com.antoine.goodCount.utils.Currency
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_spent.view.*

/**
 * A placeholder fragment containing a simple view.
 */
private const val USER_APP = "user app"
private const val ANSWER_REQUEST = 1914
private const val ANSWER_WRITING_REQUEST = "answer writing request"
private const val COMMON_POT_ID = "common pot id"
private const val LINE_COMMON_POT_ID = "line common pot id"
private const val USER = "user"
private const val USER_ID = "user id"
class SpentFragment : Fragment(), SpentClickListener {

    private lateinit var mViewOfLayout: View
    private lateinit var mSpentFragmentViewModel: SpentFragmentViewModel
    private lateinit var mAdapter: SpentFragmentRecyclerViewAdapter
    private lateinit var mCommonPotId: String
    private var mCommonPot: CommonPot? = null
    private var mLineCommonPotList: MutableList<LineCommonPot> = ArrayList()
    private var mUsername: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        mViewOfLayout = inflater.inflate(R.layout.fragment_spent, container, false)
        mCommonPotId = arguments?.getString(COMMON_POT_ID).toString()
        this.configureViewModel()
        this.getCommonPot()
        this.getLineCommonPot()
        this.configureRecyclerView()
        this.getUsername()

        mViewOfLayout.spent_fragment_add_spent_fab.setOnClickListener {
            val createSpentIntent = Intent(context, CreateSpentActivity::class.java)
            createSpentIntent.putExtra(COMMON_POT_ID, mCommonPotId)
            startActivityForResult(createSpentIntent, ANSWER_REQUEST)
        }
        return mViewOfLayout
    }

    private fun configureViewModel(){
        mSpentFragmentViewModel = ViewModelProviders.of(this).get(SpentFragmentViewModel::class.java)
    }

    private fun configureRecyclerView(){
        this.mAdapter = SpentFragmentRecyclerViewAdapter(this)
        mViewOfLayout.spent_fragment_recyclerview.adapter = this.mAdapter
        mViewOfLayout.spent_fragment_recyclerview.layoutManager = LinearLayoutManager(this.context)
        mViewOfLayout.spent_fragment_recyclerview.setHasFixedSize(true)
        this.setUpItemTouchHelper()
        this.setUpAnimationDecoratorHelper()
    }

    private fun setUpItemTouchHelper(){
        val simpleItemTouchCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT){

            lateinit var background: Drawable
            lateinit var trashMark: Drawable
            var trashMarkMargin: Int = 0
            var initiated: Boolean = false

            private fun init() {
                background = ColorDrawable(Color.RED)
                val draw = context?.let { ContextCompat.getDrawable(it, R.drawable.ic_delete) }
                if (draw != null) trashMark = draw
                trashMark.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
                trashMarkMargin = 16
                initiated = true
            }

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val swipedPosition = viewHolder.adapterPosition
                mAdapter.pendingRemoval(swipedPosition)
            }

            override fun getSwipeDirs(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
                val position = viewHolder.adapterPosition
                return if (mAdapter.isPendingRemoval(position)) {
                    0
                } else super.getSwipeDirs(recyclerView, viewHolder)
            }

            override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
                val itemView = viewHolder.itemView

                if (viewHolder.adapterPosition == -1) {
                    // not interested in those
                    return
                }

                if (!initiated) {
                    init()
                }

                // draw red background
                background.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
                background.draw(c)

                // draw trash mark
                val itemHeight = itemView.bottom - itemView.top
                val intrinsicWidth: Int = trashMark.intrinsicWidth
                val intrinsicHeight = trashMark.intrinsicWidth

                val xMarkLeft = itemView.right - trashMarkMargin - intrinsicWidth
                val xMarkRight = itemView.right - trashMarkMargin
                val xMarkTop = itemView.top + (itemHeight - intrinsicHeight) / 2
                val xMarkBottom = xMarkTop + intrinsicHeight
                trashMark.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom)

                trashMark.draw(c)
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }
        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(mViewOfLayout.spent_fragment_recyclerview)
    }

    private fun setUpAnimationDecoratorHelper(){
        mViewOfLayout.spent_fragment_recyclerview.addItemDecoration(object : RecyclerView.ItemDecoration(){

            lateinit var background: Drawable
            var initiated: Boolean = false

            private fun init() {
                background = ColorDrawable(Color.RED)
                initiated = true
            }

            override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {

                if (!initiated) {
                    init()
                }
                val itemAnimator = parent.itemAnimator
                // only if animation is in progress
                if (itemAnimator != null && itemAnimator.isRunning) {
                    var lastViewComingDown: View? = null
                    var firstViewComingUp: View? = null

                    // this is fixed
                    val left = 0
                    val right = parent.width

                    // this we need to find out
                    var top = 0
                    var bottom = 0

                    // find relevant translating views
                    val childCount = parent.layoutManager?.childCount
                    if (childCount != null){
                        for (i in 0 until childCount) {
                            val child = parent.layoutManager?.getChildAt(i)
                            if (child != null && child.translationY < 0) {
                                // view is coming down
                                lastViewComingDown = child
                            } else if (child != null && child.translationY > 0) {
                                // view is coming up
                                if (firstViewComingUp == null) {
                                    firstViewComingUp = child
                                }
                            }
                        }
                    }

                    if (lastViewComingDown != null && firstViewComingUp != null) {
                        // views are coming down AND going up to fill the void
                        top = lastViewComingDown.bottom + lastViewComingDown.translationY.toInt()
                        bottom = firstViewComingUp.top + firstViewComingUp.translationY.toInt()
                    } else if (lastViewComingDown != null) {
                        // views are going down to fill the void
                        top = lastViewComingDown.bottom + lastViewComingDown.translationY.toInt()
                        bottom = lastViewComingDown.bottom
                    } else if (firstViewComingUp != null) {
                        // views are coming up to fill the void
                        top = firstViewComingUp.top
                        bottom = firstViewComingUp.top + firstViewComingUp.translationY.toInt()
                    }

                    background.setBounds(left, top, right, bottom)
                    background.draw(c)

                }
                super.onDraw(c, parent, state)
            }
        })
    }

    // Get the LineCommonPots of the file
    private fun getLineCommonPot(){
        mSpentFragmentViewModel.getLineCommonPot(mCommonPotId).observe(this, Observer { list ->
            if (list != null){
                mLineCommonPotList = list
                if (list.size == 0){
                    mViewOfLayout.spent_fragment_no_spent_textView.visibility = View.VISIBLE
                    mViewOfLayout.spent_fragment_no_spent_add_textView.visibility = View.VISIBLE
                }else{
                    mViewOfLayout.spent_fragment_no_spent_textView.visibility = View.INVISIBLE
                    mViewOfLayout.spent_fragment_no_spent_add_textView.visibility = View.INVISIBLE
                }
                this.mAdapter.updateData(list)
                this.getTotalCost()
                this.getPersonalCost()
            }
        })
    }

    // Get the common pot for get the currency and display the title
    private fun getCommonPot(){
        mSpentFragmentViewModel.getCommonPot(mCommonPotId).observe(this, Observer { commonPot ->
            mCommonPot = commonPot
            this.mAdapter.updateCommonPot(commonPot)
            this.getTotalCost()
            this.getPersonalCost()
            activity?.title = commonPot.title
        })
    }

    // Get the username of the current user
    private fun getUsername(){
        mSpentFragmentViewModel.getParticipant(this.getUserId(), mCommonPotId).observe(this, Observer { participantList ->
            mUsername = participantList[USER_APP]?.id.toString()
            this.mAdapter.updateParticipantList(participantList)
            this.getPersonalCost()
        })
    }

    // Calculate the amount you have currently spent
    private fun getPersonalCost(){
        var personalCost = 0.0
        if (mLineCommonPotList.isNotEmpty() && mUsername.isNotEmpty()){
            for (lineCommonPot in mLineCommonPotList){
                if (lineCommonPot.paidBy == mUsername){
                    personalCost += lineCommonPot.amount
                }
            }
        }
        if (mCommonPot != null){
            val cost = Currency.formatAtCurrency(mCommonPot?.currency, personalCost)
            val personalCostPhrase = "${getString(R.string.personal_cost)}\n$cost"
            mViewOfLayout.spent_fragment_my_cost_textView.text = personalCostPhrase
        }
    }

    // Calculates the total amount spent by all participants in the common pot
    private fun getTotalCost(){
        var totalCost = 0.0
        if (mLineCommonPotList.isNotEmpty()){
            for (lineCommonPot in mLineCommonPotList){
                totalCost += lineCommonPot.amount
            }
        }
        if (mCommonPot != null){
            val cost = Currency.formatAtCurrency(mCommonPot?.currency, totalCost)
            val totalCostPhrase = "${getString(R.string.total_cost)}\n$cost"
            mViewOfLayout.spent_fragment_total_cost_textView.text = totalCostPhrase
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ANSWER_REQUEST){
            if (resultCode == Activity.RESULT_OK){
                val code = data?.getIntExtra(ANSWER_WRITING_REQUEST, -1)
                code?.let { this.displaySnackBar(it) }
            }
        }
    }

    // Displays a message in a snackBar according to the return of activities
    private fun displaySnackBar(code: Int) {
        when(code){
            0 -> {
                view?.let { Snackbar.make(it, getString(R.string.successful_creation), Snackbar.LENGTH_LONG).show() }
            }
            1 -> {
                view?.let { Snackbar.make(it, getString(R.string.error_creating), Snackbar.LENGTH_LONG).show() }
            }
            2 -> {
                view?.let { Snackbar.make(it, getString(R.string.successful_update), Snackbar.LENGTH_LONG).show() }
            }
            3 -> {
                view?.let { Snackbar.make(it, getString(R.string.error_updating), Snackbar.LENGTH_LONG).show() }
            }
            4 -> {
                view?.let { Snackbar.make(it, getString(R.string.successful_delete), Snackbar.LENGTH_LONG).show() }
            }
            5 -> {
                view?.let { Snackbar.make(it, getString(R.string.error_deleting), Snackbar.LENGTH_LONG).show() }
            }
        }
    }

    // When the user clicks on an item the onClick method is called and starts the EditSpentActivity
    override fun onClick(lineCommonPotId: String) {
        val editSpentIntent = Intent(context, EditSpentActivity::class.java)
        editSpentIntent.putExtra(COMMON_POT_ID, mCommonPotId)
        editSpentIntent.putExtra(LINE_COMMON_POT_ID, lineCommonPotId)
        startActivityForResult(editSpentIntent, ANSWER_REQUEST)
    }

    // When the user drag an item to delete it and the 3 seconds to cancel are passed the onSwipeDelete method is called to remove the common pot spent
    override fun onDeleteSwipe(lineCommonPot: LineCommonPot) {
        if (mLineCommonPotList.contains(lineCommonPot)){
            val index = mLineCommonPotList.indexOf(lineCommonPot)
            mLineCommonPotList.removeAt(index)
            getPersonalCost()
            getTotalCost()
        }
        this.removeInDatabase(lineCommonPot.id)
    }

    // Remove the spent in database
    private fun removeInDatabase(lineCommonPotId: String) {
        mSpentFragmentViewModel.getParticipantSpentAtSpent(lineCommonPotId).observe(this, Observer {participantSpentIdList ->
            mSpentFragmentViewModel.removeSpent(participantSpentIdList, lineCommonPotId).addOnSuccessListener {
                this.displaySnackBar(4)
            }.addOnFailureListener {
                this.displaySnackBar(5)
            }
        })
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    private fun getUserId(): String {
        val sharedPref: SharedPreferences = context!!.getSharedPreferences(USER, MODE_PRIVATE)
        return sharedPref.getString(USER_ID, null)
    }

    // Share the dynamic link of the common pot with other users
    fun shareCommonPot(){
        val textSend = "${getString(R.string.join_me_on_good_count)} ${mCommonPot?.shareLink}"
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, textSend)
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share)))
    }

    fun removeListener(){
        try {
            mSpentFragmentViewModel.removeListener()
        }catch (e: Exception){ }
    }

    override fun onStop() {
        super.onStop()
        this.removeListener()
    }

    companion object {
        @JvmStatic
        fun newInstance(commonPotId: String): SpentFragment {
            return SpentFragment().apply {
                arguments = Bundle().apply {
                    putString(COMMON_POT_ID, commonPotId)
                }
            }
        }
    }
}