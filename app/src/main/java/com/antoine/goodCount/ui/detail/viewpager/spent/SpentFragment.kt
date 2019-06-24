package com.antoine.goodCount.ui.detail.viewpager.spent

import android.app.Activity
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
import com.antoine.goodCount.ui.detail.viewpager.spent.recyclerView.SpentFragmentRecyclerViewAdapter
import com.antoine.goodCount.utils.Currency
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_spent.view.*

/**
 * A placeholder fragment containing a simple view.
 */
private const val USER_APP = "user app"
private const val ANSWER_REQUEST = 1914
private const val ANSWER_WRITING_REQUEST = "answer writing request"
private const val COMMON_POT_ID = "common pot id"
private const val USER = "user"
private const val USER_ID = "user id"
class SpentFragment : Fragment() {

    private lateinit var mViewOfLayout: View
    private lateinit var mSpentFragmentViewModel: SpentFragmentViewModel
    private lateinit var mAdapter: SpentFragmentRecyclerViewAdapter
    private lateinit var mCommonPotId: String
    private var mCommonPot: CommonPot? = null
    private var mLineCommonPotList: List<LineCommonPot> = ArrayList()
    private var mUsername: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        mViewOfLayout = inflater.inflate(R.layout.fragment_spent, container, false)
        mCommonPotId = arguments?.getString(COMMON_POT_ID).toString()
        this.configureViewModel()
        this.getCommonPot()
        this.getLineCommonPot()
        this.configureRecyclerView()
        this.getUsername()

        mViewOfLayout.spent_fragment_add_spent_fab.setOnClickListener {
            val createSpentIntent = Intent(context, CreateSpentActivity::class.java)
            createSpentIntent.putExtra(COMMON_POT_ID, mCommonPotId)
            startActivityForResult(createSpentIntent, ANSWER_REQUEST)
        }
        return mViewOfLayout
    }

    private fun configureViewModel(){
        mSpentFragmentViewModel = ViewModelProviders.of(this).get(SpentFragmentViewModel::class.java)
    }

    private fun configureRecyclerView(){
        this.mAdapter = SpentFragmentRecyclerViewAdapter()
        mViewOfLayout.spent_fragment_recyclerview.adapter = this.mAdapter
        mViewOfLayout.spent_fragment_recyclerview.layoutManager = LinearLayoutManager(this.context)
    }

    private fun getLineCommonPot(){
        mSpentFragmentViewModel.getLineCommonPot(mCommonPotId).observe(this, Observer { list ->
            mLineCommonPotList = list
            this.mAdapter.updateData(list)
            this.getTotalCost()
            this.getPersonalCost()
        })
    }

    private fun getCommonPot(){
        mSpentFragmentViewModel.getCommonPot(mCommonPotId).observe(this, Observer { commonPot ->
            mCommonPot = commonPot
            this.mAdapter.updateCommonPot(commonPot)
            this.getTotalCost()
            this.getPersonalCost()
        })
    }

    private fun getUsername(){
        mSpentFragmentViewModel.getParticipant(this.getUserId(), mCommonPotId).observe(this, Observer { participantList ->
            mUsername = participantList[USER_APP]?.id.toString()
            this.mAdapter.updateParticipantList(participantList)
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
            val cost = Currency.formatAtCurrency(mCommonPot?.currency, personalCost)
            val personalCostPhrase = "${getString(R.string.personal_cost)}\n$cost"
            mViewOfLayout.spent_fragment_my_cost_textView.text = personalCostPhrase
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
            val cost = Currency.formatAtCurrency(mCommonPot?.currency, totalCost)
            val totalCostPhrase = "${getString(R.string.total_cost)}\n$cost"
            mViewOfLayout.spent_fragment_total_cost_textView.text = totalCostPhrase
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ANSWER_REQUEST){
            if (resultCode == Activity.RESULT_OK){
                val code = data?.getIntExtra(ANSWER_WRITING_REQUEST, -1)
                code?.let { this.displaySnackBar(it) }
            }
        }
    }

    private fun displaySnackBar(code: Int) {
        when(code){
            0 -> {
                view?.let { Snackbar.make(it, getString(R.string.successful_creation), Snackbar.LENGTH_LONG).show() }
            }
            1 -> {
                view?.let { Snackbar.make(it, getString(R.string.error_creating), Snackbar.LENGTH_LONG).show() }
            }
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