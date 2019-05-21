package com.antoine.goodCount.ui.main


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.antoine.goodCount.R
import com.antoine.goodCount.models.CommonPot

/**
 * A simple [Fragment] subclass.
 *
 */
private const val ID_USER = "id user"
class MainFragment : Fragment() {

    private lateinit var mMainViewModel: MainViewModel
    private lateinit var commonPotList : List<CommonPot>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        val userId: String = arguments?.get(ID_USER).toString()
        this.configureViewModel()
        this.getCommonPot(userId)

        return view
    }

    private fun configureViewModel(){
        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

    private fun getCommonPot(userId: String){
        mMainViewModel.getParticipantCommonPot(userId) .observe(this, Observer { it->
            commonPotList = it
            if (commonPotList.isNotEmpty()){
                Log.e("TAG Fragment", "${commonPotList[0].tittle}")
            }

        })
    }
}
