package com.antoine.goodCount.ui.main


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders

import com.antoine.goodCount.R

/**
 * A simple [Fragment] subclass.
 *
 */
private const val ID_USER = "id user"
class MainFragment : Fragment() {

    private lateinit var mMainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        val userId: String = arguments?.get(ID_USER).toString()
        this.configureViewModel()

        return view
    }

    private fun configureViewModel(){
        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
    }
}
