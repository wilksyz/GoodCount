package com.antoine.goodCount.ui.detail.viewpager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.antoine.goodCount.R

class BalanceFragment : Fragment() {

    private lateinit var mViewOfLayout: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mViewOfLayout = inflater.inflate(R.layout.fragment_balance, container, false)



        return mViewOfLayout
    }
}
