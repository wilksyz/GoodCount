package com.antoine.goodCount.ui.createAndEdit.edit

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.antoine.goodCount.R
import com.antoine.goodCount.models.CommonPot
import com.antoine.goodCount.models.Participant
import com.antoine.goodCount.ui.createAndEdit.BaseCommonPotActivity
import com.antoine.goodCount.utils.Currency
import kotlinx.android.synthetic.main.activity_create_common_pot.*

private const val COMMON_POT_ID = "common pot id"
private const val ANSWER_WRITING_REQUEST = "answer writing request"
class EditCommonPotActivity: BaseCommonPotActivity() {

    private lateinit var mCommonPotId: String
    private lateinit var mEditViewModel: EditViewModel
    private lateinit var mCommonPot: CommonPot
    private lateinit var mParticipant: Participant

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mCommonPotId = intent?.getStringExtra(COMMON_POT_ID).toString()
        this.configureViewModel()
        this.getCommonPot()
        this.getUsername(getUserId(), mCommonPotId)
        create_activity_add_common_pot_button.setOnClickListener {
            this.retrieveInformation()
        }
    }

    private fun configureViewModel(){
        mEditViewModel = ViewModelProviders.of(this).get(EditViewModel::class.java)
    }

    private fun configureSpinner(mCurrencyList: List<String>, currencyCode: String) {
        mPositionSpinner = Currency.getCurrencySelected(mCurrencyList, currencyCode)
        create_activity_spinner.text = SpannableStringBuilder(mCurrencyList[mPositionSpinner])
        settingUpSpinner()
    }

    private fun getCommonPot(){
        mEditViewModel.getCommonPot(mCommonPotId).observe(this, Observer {
            if (it != null){
                mCommonPot = it
                this.updateUI()
                this.configureSpinner(mCurrencyList, mCommonPot.currency)
            }
        })
    }

    private fun updateUI() {
        if (create_activity_title_editext.text.toString().isEmpty()){
            create_activity_title_editext.text = SpannableStringBuilder(mCommonPot.title)
        }
        if (create_activity_description_editext.text.toString().isEmpty()){
            create_activity_description_editext.text = SpannableStringBuilder(mCommonPot.description)
        }
        create_activity_add_common_pot_button.text = getString(R.string.validate_the_change)
    }

    private fun getUsername(userId: String, mCommonPotId: String) {
        mEditViewModel.getUsername(userId, mCommonPotId).observe(this, Observer {
            if (it != null){
                mParticipant = it
                create_activity_your_name_editext.text = SpannableStringBuilder(mParticipant.username)
            }
        })
    }

    private fun retrieveInformation() {
        mCommonPot.title = create_activity_title_editext.text.toString()
        mCommonPot.description = create_activity_description_editext.text.toString()
        mParticipant.username = create_activity_your_name_editext.text.toString()
        mCommonPot.currency = Currency.getCurrencyCode(create_activity_spinner.text.toString())
        this.updateCommonPot()
    }

    private fun updateCommonPot(){
        val returnIntent = Intent()
        mEditViewModel.updateCommonPot(mCommonPot, mParticipant).addOnSuccessListener {
            returnIntent.putExtra(ANSWER_WRITING_REQUEST, 0)
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }.addOnFailureListener {
            returnIntent.putExtra(ANSWER_WRITING_REQUEST, 1)
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            this.onBackPressed()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }
}
