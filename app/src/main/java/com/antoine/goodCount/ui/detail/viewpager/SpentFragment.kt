package com.antoine.goodCount.ui.detail.viewpager

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.antoine.goodCount.R
import com.antoine.goodCount.models.CommonPot
import com.antoine.goodCount.ui.createSpent.CreateSpentActivity
import com.antoine.goodCount.ui.detail.DetailViewModel
import com.antoine.goodCount.ui.detail.recyclerView.DetailRecyclerViewAdapter
import kotlinx.android.synthetic.main.fragment_spent.view.*

/**
 * A placeholder fragment containing a simple view.
 */
private const val COMMON_POT_ID = "common pot id"
class SpentFragment : Fragment() {

    private lateinit var viewOfLayout: View
    private lateinit var mDetailViewModel: DetailViewModel
    private lateinit var mAdapter: DetailRecyclerViewAdapter
    private lateinit var mCommonPotId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        viewOfLayout = inflater.inflate(R.layout.fragment_spent, container, false)
        mCommonPotId = arguments?.getString(COMMON_POT_ID).toString()
        this.configureViewModel()
        this.getCommonPot()
        this.configureRecyclerView()
        this.getLineCommonPot()

        viewOfLayout.spent_fragment_add_spent_fab.setOnClickListener {
            val createSpentIntent = Intent(context, CreateSpentActivity::class.java)
            startActivity(createSpentIntent)
        }
        return viewOfLayout
    }

    private fun configureViewModel(){
        mDetailViewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java)
    }

    private fun configureRecyclerView(){
        this.mAdapter = DetailRecyclerViewAdapter()
        viewOfLayout.spent_fragment_recyclerview.adapter = this.mAdapter
        viewOfLayout.spent_fragment_recyclerview.layoutManager = LinearLayoutManager(this.context)
    }

    private fun getLineCommonPot(){
        mDetailViewModel.getLineCommonPot(mCommonPotId).observe(this, Observer { list ->
            this.mAdapter.updateData(list)
        })
    }

    private fun getCommonPot(){
        mDetailViewModel.getCommonPot(mCommonPotId).observe(this, Observer { commonPot ->
            this.mAdapter.updateCommonPot(commonPot)
        })
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