package com.antoine.goodCount.ui.detail.viewpager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.antoine.goodCount.R
import com.antoine.goodCount.ui.detail.DetailViewModel
import kotlinx.android.synthetic.main.fragment_detail.view.*

/**
 * A placeholder fragment containing a simple view.
 */
class SpentFragment : Fragment() {

    private lateinit var mDetailViewModel: DetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val viewOfLayout = inflater.inflate(R.layout.fragment_detail, container, false)
        this.configureViewModel()
        
        return viewOfLayout
    }

    private fun configureViewModel(){
        mDetailViewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java)
    }
}