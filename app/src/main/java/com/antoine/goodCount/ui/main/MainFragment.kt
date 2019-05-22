package com.antoine.goodCount.ui.main


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager

import com.antoine.goodCount.R
import com.antoine.goodCount.models.CommonPot
import com.antoine.goodCount.ui.main.recyclerview.MainRecyclerViewAdapter
import kotlinx.android.synthetic.main.fragment_main.view.*

/**
 * A simple [Fragment] subclass.
 *
 */
private const val ID_USER = "id user"
class MainFragment : Fragment() {

    private lateinit var viewOfLayout: View
    private lateinit var mAdapter: MainRecyclerViewAdapter
    private lateinit var mMainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        viewOfLayout = inflater.inflate(R.layout.fragment_main, container, false)
        val userId: String = arguments?.get(ID_USER).toString()
        this.configureViewModel()
        this.configureRecyclerView()
        this.getCommonPot(userId)

        return viewOfLayout
    }

    private fun configureViewModel(){
        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

    private fun configureRecyclerView(){
        this.mAdapter = MainRecyclerViewAdapter()
        viewOfLayout.main_fragment_recycler_view.adapter = this.mAdapter
        viewOfLayout.main_fragment_recycler_view.layoutManager = LinearLayoutManager(this.context)
    }

    private fun getCommonPot(userId: String){
        mMainViewModel.getParticipantCommonPot(userId) .observe(this, Observer {
            if (it.isNotEmpty()){
                mAdapter.updateData(it)
            }
        })
    }
}
