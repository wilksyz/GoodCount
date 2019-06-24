package com.antoine.goodCount.ui.detail.viewpager.balance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.antoine.goodCount.R
import com.antoine.goodCount.ui.detail.viewpager.balance.recyclerView.BalanceFragmentRecyclerViewAdapter
import kotlinx.android.synthetic.main.fragment_balance.view.*

private const val COMMON_POT_ID = "common pot id"
class BalanceFragment : Fragment() {

    private lateinit var mViewOfLayout: View
    private lateinit var mCommonPotId: String
    private lateinit var mAdapter: BalanceFragmentRecyclerViewAdapter
    private lateinit var mBalanceFragmentViewModel: BalanceFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mViewOfLayout = inflater.inflate(R.layout.fragment_balance, container, false)
        mCommonPotId = arguments?.getString(COMMON_POT_ID).toString()
        this.configureViewModel()
        this.configureRecyclerView()
        this.getParticipantBalance()
        this.getCommonPot()

        return mViewOfLayout
    }

    private fun configureViewModel(){
        mBalanceFragmentViewModel = ViewModelProviders.of(this).get(BalanceFragmentViewModel::class.java)
    }

    private fun configureRecyclerView(){
        this.mAdapter = BalanceFragmentRecyclerViewAdapter()
        mViewOfLayout.balance_fragment_recyclerview.adapter = this.mAdapter
        mViewOfLayout.balance_fragment_recyclerview.layoutManager = LinearLayoutManager(this.context)
    }

    private fun getParticipantBalance(){
        mBalanceFragmentViewModel.getParticipant(mCommonPotId).observe(this, Observer {
            mAdapter.updateData(it, mBalanceFragmentViewModel.mParticipantList)
        })
    }

    private fun getCommonPot(){
        mBalanceFragmentViewModel.getCommonPot(mCommonPotId).observe(this, Observer { commonPot ->
            mAdapter.updateCurrency(commonPot.currency)
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(commonPotId: String): BalanceFragment {
            return BalanceFragment().apply {
                arguments = Bundle().apply {
                    putString(COMMON_POT_ID, commonPotId)
                }
            }
        }
    }
}
