package com.antoine.goodCount.ui.main.fragment


import android.app.Activity
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.antoine.goodCount.R
import com.antoine.goodCount.models.CommonPot
import com.antoine.goodCount.ui.createAndEdit.create.CreateCommonPotActivity
import com.antoine.goodCount.ui.detail.DetailActivity
import com.antoine.goodCount.ui.main.recyclerView.ClickListener
import com.antoine.goodCount.ui.main.recyclerView.MainRecyclerViewAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_main.view.*

/**
 * A simple [Fragment] subclass.
 *
 */
private const val USER_ID = "user id"
private const val COMMON_POT_ID = "common pot id"
private const val ANSWER_REQUEST_CREATE = 1916
private const val ANSWER_REQUEST = 1924
private const val ANSWER_WRITING_REQUEST = "answer writing request"
class MainFragment : Fragment(), ClickListener {

    private lateinit var mViewOfLayout: View
    private lateinit var mAdapter: MainRecyclerViewAdapter
    private lateinit var mMainFragmentViewModel: MainFragmentViewModel
    private var mIsOpen = false
    private lateinit var mFabClose: Animation
    private lateinit var mFabOpen: Animation
    private lateinit var mFabClock: Animation
    private lateinit var mFabAntiClock: Animation
    private lateinit var mUserId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mViewOfLayout = inflater.inflate(R.layout.fragment_main, container, false)
        mUserId = arguments?.get(USER_ID).toString()
        this.configureAnimation()
        this.configureViewModel()
        this.configureRecyclerView()
        this.getCommonPot(mUserId)

        mViewOfLayout.main_fragment_floating_action_button.setOnClickListener {
            this.setContextualMenu()
        }
        mViewOfLayout.main_fragment_recycler_view.setOnTouchListener { _, _ ->
            if (mIsOpen){
                this.setContextualMenu()
            }
            false
        }
        mViewOfLayout.main_fragment_button_add_common_pot.setOnClickListener {
            this.setContextualMenu()
            val intent = Intent(context, CreateCommonPotActivity::class.java)
            startActivityForResult(intent, ANSWER_REQUEST_CREATE)
        }
        mViewOfLayout.main_fragment_button_join_common_pot.setOnClickListener {
            this.setContextualMenu()
            view?.let { it1 -> Snackbar.make(it1, getString(R.string.ask_your_friends_for_the_pot_sharing_code), Snackbar.LENGTH_LONG).show() }
        }
        return mViewOfLayout
    }

    private fun configureViewModel(){
        mMainFragmentViewModel = ViewModelProviders.of(this).get(MainFragmentViewModel::class.java)
    }

    private fun configureRecyclerView(){
        this.mAdapter = MainRecyclerViewAdapter(this)
        mViewOfLayout.main_fragment_recycler_view.adapter = this.mAdapter
        mViewOfLayout.main_fragment_recycler_view.layoutManager = LinearLayoutManager(this.context)
        mViewOfLayout.main_fragment_recycler_view.setHasFixedSize(true)
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
        itemTouchHelper.attachToRecyclerView(mViewOfLayout.main_fragment_recycler_view)
    }

    private fun setUpAnimationDecoratorHelper(){
        mViewOfLayout.main_fragment_recycler_view.addItemDecoration(object : RecyclerView.ItemDecoration(){

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

    private fun configureAnimation(){
        mFabClose = AnimationUtils.loadAnimation(context, R.anim.fab_close)
        mFabOpen = AnimationUtils.loadAnimation(context, R.anim.fab_open)
        mFabClock = AnimationUtils.loadAnimation(context, R.anim.fab_rotate_clock)
        mFabAntiClock = AnimationUtils.loadAnimation(context, R.anim.fab_rotate_anticlock)
    }

    private fun setContextualMenu(){
        if (mIsOpen) {
            main_fragment_create_CardView.visibility = View.INVISIBLE
            main_fragment_join_cardView.visibility = View.INVISIBLE
            main_fragment_button_join_common_pot.startAnimation(mFabClose)
            main_fragment_button_add_common_pot.startAnimation(mFabClose)
            main_fragment_floating_action_button.startAnimation(mFabAntiClock)
            main_fragment_button_join_common_pot.isClickable = false
            main_fragment_button_add_common_pot.isClickable = false
            mIsOpen = false
        } else {
            main_fragment_create_CardView.visibility = View.VISIBLE
            main_fragment_join_cardView.visibility = View.VISIBLE
            main_fragment_button_join_common_pot.startAnimation(mFabOpen)
            main_fragment_button_add_common_pot.startAnimation(mFabOpen)
            main_fragment_floating_action_button.startAnimation(mFabClock)
            main_fragment_button_join_common_pot.isClickable = true
            main_fragment_button_add_common_pot.isClickable = true
            mIsOpen = true
        }
    }

    private fun getCommonPot(userId: String){
        mMainFragmentViewModel.getParticipantCommonPot(userId).observe(this, Observer {
            if (it.isNotEmpty()){
                mAdapter.updateData(it as MutableList<CommonPot>)
                mViewOfLayout.main_fragment_no_good_count_textView.visibility = View.INVISIBLE
                mViewOfLayout.main_fragment_no_good_count_add_textView.visibility = View.INVISIBLE
            }else{
                mViewOfLayout.main_fragment_no_good_count_textView.visibility = View.VISIBLE
                mViewOfLayout.main_fragment_no_good_count_add_textView.visibility = View.VISIBLE
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ANSWER_REQUEST_CREATE){
            if (resultCode == Activity.RESULT_OK){
                val code = data?.getIntExtra(ANSWER_WRITING_REQUEST, -1)
                code?.let { this.displaySnackBar(it) }
            }
        }
        if (requestCode == ANSWER_REQUEST){
            if (resultCode == Activity.RESULT_OK){
                val code = data?.getIntExtra(ANSWER_WRITING_REQUEST, -1)
                code?.let { this.displaySnackBar(it) }
            }
        }
    }

    private fun displaySnackBar(code: Int) {
        when(code){
            0 -> {
                view?.let { Snackbar.make(it, getString(R.string.successful_creation), Snackbar.LENGTH_LONG).show() }
            }
            1 -> {
                view?.let { Snackbar.make(it, getString(R.string.error_creating), Snackbar.LENGTH_LONG).show() }
            }
            2 -> {
                view?.let { Snackbar.make(it, getString(R.string.successful_delete), Snackbar.LENGTH_LONG).show() }
            }
            3 -> {
                view?.let { Snackbar.make(it, getString(R.string.error_deleting), Snackbar.LENGTH_LONG).show() }
            }
            4 -> {
                view?.let { Snackbar.make(it, getString(R.string.remove_from_your_list), Snackbar.LENGTH_LONG).show() }
            }
            5 -> {
                view?.let { Snackbar.make(it, getString(R.string.failed_to_delete_your_list), Snackbar.LENGTH_LONG).show() }
            }
        }
    }

    override fun onClick(position: Int) {
        val detailActivityIntent = Intent(context, DetailActivity::class.java)
        detailActivityIntent.putExtra(COMMON_POT_ID, mAdapter.getCommonPotId(position))
        startActivityForResult(detailActivityIntent, ANSWER_REQUEST)
    }

    override fun onUndoClick(commonPot: CommonPot) {
        mMainFragmentViewModel.takeOffParticipant(commonPot, mUserId).observe(this, Observer {indicator ->
            if (indicator != null){
                if (indicator){
                    this.displaySnackBar(4)
                }else{
                    this.displaySnackBar(5)
                }
            }
        })
    }
}


