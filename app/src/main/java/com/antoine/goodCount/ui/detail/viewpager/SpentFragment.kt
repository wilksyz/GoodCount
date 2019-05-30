package com.antoine.goodCount.ui.detail.viewpager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.antoine.goodCount.R
import com.antoine.goodCount.ui.detail.DetailViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_spent.view.*

/**
 * A placeholder fragment containing a simple view.
 */
class SpentFragment : Fragment() {

    private lateinit var mDetailViewModel: DetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val viewOfLayout = inflater.inflate(R.layout.fragment_spent, container, false)
        this.configureViewModel()

        viewOfLayout.spent_fragment_add_spent_fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with an action", Snackbar.LENGTH_LONG).show()
        }
        return viewOfLayout
    }

    private fun configureViewModel(){
        mDetailViewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java)
    }
}