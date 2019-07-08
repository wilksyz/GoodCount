package com.antoine.goodCount.ui.createAndEdit.create

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.SpannableStringBuilder
import androidx.lifecycle.ViewModelProviders
import com.antoine.goodCount.R
import com.antoine.goodCount.models.CommonPot
import com.antoine.goodCount.models.Participant
import com.antoine.goodCount.ui.createAndEdit.BaseCommonPotActivity
import com.antoine.goodCount.utils.Currency
import kotlinx.android.synthetic.main.activity_create_common_pot.*

private const val ANSWER_WRITING_REQUEST = "answer writing request"
class CreateCommonPotActivity: BaseCommonPotActivity() {

    private lateinit var mCreateViewModel: CreateViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.title = getString(R.string.create)
        this.configureViewModel()
        this.configureSpinner()

        create_activity_add_common_pot_button.setOnClickListener {
            this.retrieveInformation()
        }
    }

    private fun configureViewModel(){
        mCreateViewModel = ViewModelProviders.of(this).get(CreateViewModel::class.java)
    }

    private fun configureSpinner(){
        mPositionSpinner = Currency.getLocaleCurrency(mCurrencyList)
        create_activity_spinner.text = SpannableStringBuilder(mCurrencyList[mPositionSpinner])
        settingUpSpinner()
    }

    // Get the information entered to create the CommonPot
    private fun retrieveInformation(){
        val title = create_activity_title_editext.text.toString()
        val description = create_activity_description_editext.text.toString()
        val name = create_activity_your_name_editext.text.toString()
        val currency = Currency.getCurrencyCode(create_activity_spinner.text.toString())
        val userId = this.getUserId()
        val commonPot = CommonPot("", title, description, currency, "")
        val participant = Participant("","", userId,name, true)
        this.createCommonPot(commonPot, participant)
    }

    private fun createCommonPot(commonPot: CommonPot, participant: Participant){
        val returnIntent = Intent()
        mCreateViewModel.createCommonPot(commonPot, participant).addOnSuccessListener {
            returnIntent.putExtra(ANSWER_WRITING_REQUEST, 0)
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }.addOnFailureListener {
            returnIntent.putExtra(ANSWER_WRITING_REQUEST, 1)
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }
    }
}
