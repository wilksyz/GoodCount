package com.antoine.goodCount.ui.detail.viewpager.balance

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.antoine.goodCount.R

private const val COMMON_POT_ID = "common pot id"
class BalanceFragment : Fragment() {

    private lateinit var mViewOfLayout: View
    private lateinit var mCommonPotId: String
    private lateinit var mBalanceFragmentViewModel: BalanceFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mViewOfLayout = inflater.inflate(R.layout.fragment_balance, container, false)
        mCommonPotId = arguments?.getString(COMMON_POT_ID).toString()
        this.configureViewModel()
        this.getParticipant()

        return mViewOfLayout
    }

    private fun configureViewModel(){
        mBalanceFragmentViewModel = ViewModelProviders.of(this).get(BalanceFragmentViewModel::class.java)
    }

    private fun getParticipant(){
        mBalanceFragmentViewModel.getParticipant(mCommonPotId).observe(this, Observer {
            Log.e("TAG","key: ${it.keys} Map: ${it.values}")
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
