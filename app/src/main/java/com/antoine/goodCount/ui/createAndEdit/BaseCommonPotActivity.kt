package com.antoine.goodCount.ui.createAndEdit

import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.antoine.goodCount.R
import com.antoine.goodCount.utils.Currency
import icepick.Icepick
import kotlinx.android.synthetic.main.activity_create_common_pot.*

private const val USER = "user"
private const val USER_ID = "user id"
private const val POSITION_SPINNER_CURRENCY = "position spinner currency"
abstract class BaseCommonPotActivity: AppCompatActivity() {

    protected var mPositionSpinner:Int = 0
    protected var mCurrencyList: List<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_common_pot)
        Icepick.restoreInstanceState(this, savedInstanceState)
        if (savedInstanceState != null) mPositionSpinner = savedInstanceState.getInt(POSITION_SPINNER_CURRENCY)
        mCurrencyList = Currency.getAllCurrency()

        create_activity_title_editext.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkInformationIsEntered(0)
            }
        })
        create_activity_your_name_editext.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkInformationIsEntered(1)
            }
        })
    }

    protected fun settingUpSpinner(){
        create_activity_spinner.keyListener = null
        val adapter = ArrayAdapter(this, R.layout.dropdown_menu_popup_item, mCurrencyList)
        create_activity_spinner.setAdapter(adapter)
        create_activity_spinner.setOnItemClickListener { _, _, position, _ ->
            mPositionSpinner = position
        }
    }

    private fun checkInformationIsEntered(signInCode: Int){
        val title = create_activity_title_editext.text.toString().isNotEmpty()
        val name = create_activity_your_name_editext.text.toString().isNotEmpty()
        create_activity_add_common_pot_button.isEnabled = title && name
        if (signInCode == 0){
            if (!title){
                create_common_pot_title_textInputLayout.error = getString(R.string.you_must_add_a_title)
            }else{
                create_common_pot_title_textInputLayout.error = null
            }
        }else if (signInCode == 1){
            if (!name){
                create_common_pot_name_textInputLayout.error = getString(R.string.you_must_enter_your_username)
            }else{
                create_common_pot_name_textInputLayout.error = null
            }
        }
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    protected fun getUserId(): String {
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