package com.antoine.goodCount.ui.main


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.antoine.goodCount.R
import com.antoine.goodCount.ui.createAndEdit.CreateCommonPotActivity
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
class MainFragment : Fragment(), ClickListener {

    private lateinit var viewOfLayout: View
    private lateinit var mAdapter: MainRecyclerViewAdapter
    private lateinit var mMainViewModel: MainViewModel
    private var mIsOpen = false
    private lateinit var mFabClose: Animation
    private lateinit var mFabOpen: Animation
    private lateinit var mFabClock: Animation
    private lateinit var mFabAntiClock: Animation

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        viewOfLayout = inflater.inflate(R.layout.fragment_main, container, false)
        val userId: String = arguments?.get(USER_ID).toString()
        this.configureAnimation()
        this.configureViewModel()
        this.configureRecyclerView()
        this.getCommonPot(userId)

        viewOfLayout.main_fragment_floating_action_button.setOnClickListener {
            this.setContextualMenu()
        }
        viewOfLayout.main_fragment_button_add_common_pot.setOnClickListener {
            this.setContextualMenu()
            val intent = Intent(context, CreateCommonPotActivity::class.java)
            startActivity(intent)
        }
        viewOfLayout.main_fragment_button_join_common_pot.setOnClickListener {
            this.setContextualMenu()
            view?.let { it1 -> Snackbar.make(it1, "Ask your friends for the pot sharing code", Snackbar.LENGTH_LONG).show() }
        }
        return viewOfLayout
    }

    private fun configureViewModel(){
        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

    private fun configureRecyclerView(){
        this.mAdapter = MainRecyclerViewAdapter(this)
        viewOfLayout.main_fragment_recycler_view.adapter = this.mAdapter
        viewOfLayout.main_fragment_recycler_view.layoutManager = LinearLayoutManager(this.context)
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
        mMainViewModel.getParticipantCommonPot(userId).observe(this, Observer {
            if (it.isNotEmpty()){
                mAdapter.updateData(it)
            }
        })
    }

    override fun onClick(position: Int) {
        val detailActivityIntent = Intent(context, DetailActivity::class.java)
        detailActivityIntent.putExtra(COMMON_POT_ID, mAdapter.getCommonPotId(position))
        startActivity(detailActivityIntent)
    }

    override fun onLongClick(position: Int): Boolean {
        Log.e("TAG", "Je test le click long position: $position")
        return true
    }
}


