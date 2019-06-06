package com.antoine.goodCount.ui.createAndEdit

import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.antoine.goodCount.R
import com.antoine.goodCount.models.CommonPot
import com.antoine.goodCount.models.Participant
import icepick.Icepick
import kotlinx.android.synthetic.main.activity_create_common_pot.*

private const val USER = "user"
private const val USER_ID = "user id"
private const val POSITION_SPINNER_CURRENCY = "position spinner currency"
class CreateCommonPotActivity : AppCompatActivity() {

    private lateinit var mCreateViewModel: CreateViewModel
    private var mCurrencyList: List<String> = ArrayList()
    private var mPositionSpinner:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_common_pot)
        Icepick.restoreInstanceState(this, savedInstanceState)
        if (savedInstanceState != null) mPositionSpinner = savedInstanceState.getInt(POSITION_SPINNER_CURRENCY)
        this.configureViewModel()
        this.configureSpinner()

        create_activity_title_editext.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkInformationIsEntered()
            }
        })
        create_activity_your_name_editext.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkInformationIsEntered()
            }
        })
        create_activity_add_common_pot_button.setOnClickListener {
            this.retrieveInformation()
        }
    }

    private fun configureViewModel(){
        mCreateViewModel = ViewModelProviders.of(this).get(CreateViewModel::class.java)
    }

    private fun configureSpinner(){
        mCurrencyList = mCreateViewModel.getAllCurrency()
        mPositionSpinner = mCreateViewModel.getLocaleCurrency(mCurrencyList)
        create_activity_spinner.text = SpannableStringBuilder(mCurrencyList[mPositionSpinner])
        val adapter = ArrayAdapter(this@CreateCommonPotActivity, R.layout.dropdown_menu_popup_item, mCurrencyList)
        create_activity_spinner.setAdapter(adapter)
        create_activity_spinner.setOnItemClickListener { _, _, position, _ ->
            mPositionSpinner = position
        }
    }

    private fun checkInformationIsEntered(){
        val title = create_activity_title_editext.text.toString().isNotEmpty()
        val name = create_activity_your_name_editext.text.toString().isNotEmpty()
        create_activity_add_common_pot_button.isEnabled = title && name
    }

    private fun retrieveInformation(){
        val title = create_activity_title_editext.text.toString()
        val description = create_activity_description_editext.text.toString()
        val name = create_activity_your_name_editext.text.toString()
        val currency = mCreateViewModel.getCurrencyCode(create_activity_spinner.text.toString())
        val userId = this.getUserId()
        val commonPot = CommonPot("", title, description, currency)
        val participant = Participant("","", userId,name,0.0)
        this.createCommonPot(commonPot, participant)
    }

    private fun createCommonPot(commonPot: CommonPot, participant: Participant){
        mCreateViewModel.createCommonPot(commonPot, participant)
        finish()
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    private fun getUserId(): String {
        val sharedPref: SharedPreferences = getSharedPreferences(USER, MODE_PRIVATE)
        return sharedPref.getString(USER_ID, null)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Icepick.saveInstanceState(this, outState)
        outState.run {
            outState.putInt(POSITION_SPINNER_CURRENCY, mPositionSpinner)
        }
    }

    override fun onStop() {
        create_activity_spinner.dismissDropDown()
        super.onStop()
    }
}
