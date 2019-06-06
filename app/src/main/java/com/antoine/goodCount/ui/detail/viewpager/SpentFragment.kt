package com.antoine.goodCount.ui.detail.viewpager

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.antoine.goodCount.R
import com.antoine.goodCount.models.CommonPot
import com.antoine.goodCount.models.LineCommonPot
import com.antoine.goodCount.ui.createSpent.CreateSpentActivity
import com.antoine.goodCount.ui.detail.DetailViewModel
import com.antoine.goodCount.ui.detail.recyclerView.DetailRecyclerViewAdapter
import kotlinx.android.synthetic.main.fragment_spent.view.*

/**
 * A placeholder fragment containing a simple view.
 */
private const val COMMON_POT_ID = "common pot id"
private const val USER = "user"
private const val USER_ID = "user id"
class SpentFragment : Fragment() {

    private lateinit var viewOfLayout: View
    private lateinit var mDetailViewModel: DetailViewModel
    private lateinit var mAdapter: DetailRecyclerViewAdapter
    private lateinit var mCommonPotId: String
    private var mCommonPot: CommonPot? = null
    private var mLineCommonPotList: List<LineCommonPot> = ArrayList()
    private var mUsername: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        viewOfLayout = inflater.inflate(R.layout.fragment_spent, container, false)
        mCommonPotId = arguments?.getString(COMMON_POT_ID).toString()
        this.configureViewModel()
        this.getCommonPot()
        this.getLineCommonPot()
        this.configureRecyclerView()
        this.getUsername()

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
            mLineCommonPotList = list
            this.mAdapter.updateData(list)
            this.getTotalCost()
            this.getPersonalCost()
        })
    }

    private fun getCommonPot(){
        mDetailViewModel.getCommonPot(mCommonPotId).observe(this, Observer { commonPot ->
            mCommonPot = commonPot
            this.mAdapter.updateCommonPot(commonPot)
            this.getTotalCost()
        })
    }

    private fun getUsername(){
        mDetailViewModel.getUsername(this.getUserId(), mCommonPotId).observe(this, Observer { username ->
            mUsername = username
            this.mAdapter.updateUsername(username)
            this.getPersonalCost()
        })
    }

    private fun getPersonalCost(){
        var personalCost = 0.0
        if (mLineCommonPotList.isNotEmpty() && mUsername.isNotEmpty()){
            for (lineCommonPot in mLineCommonPotList){
                if (lineCommonPot.paidBy == mUsername){
                    personalCost += lineCommonPot.amount
                }
            }
        }
        if (mCommonPot != null){
            val cost = mDetailViewModel.formatAtCurrency(mCommonPot?.currency, personalCost)
            val personalCostPhrase = "${getString(R.string.personal_cost)}\n$cost"
            viewOfLayout.spent_fragment_my_cost_textView.text = personalCostPhrase
        }
    }

    private fun getTotalCost(){
        var totalCost = 0.0
        if (mLineCommonPotList.isNotEmpty()){
            for (lineCommonPot in mLineCommonPotList){
                totalCost += lineCommonPot.amount
            }
        }
        if (mCommonPot != null){
            val cost = mDetailViewModel.formatAtCurrency(mCommonPot?.currency, totalCost)
            val totalCostPhrase = "${getString(R.string.total_cost)}\n$cost"
            viewOfLayout.spent_fragment_total_cost_textView.text = totalCostPhrase
        }
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    private fun getUserId(): String {
        val sharedPref: SharedPreferences = context!!.getSharedPreferences(USER, MODE_PRIVATE)
        return sharedPref.getString(USER_ID, null)
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