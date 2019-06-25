package com.antoine.goodCount.ui.createAndEditSpent

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.antoine.goodCount.R
import com.antoine.goodCount.ui.createAndEditSpent.recyclerView.ClickListener
import com.antoine.goodCount.ui.createAndEditSpent.recyclerView.CreateSpentRecyclerViewAdapter
import icepick.Icepick
import kotlinx.android.synthetic.main.activity_create_spent.*

private const val POSITION_SPINNER_PAID_BY = "position spinner paid by"
private const val IS_SELECTED_MAP = "is selected map"
abstract class BaseSpentActivity: AppCompatActivity(), ClickListener {

    protected lateinit var mAdapter: CreateSpentRecyclerViewAdapter
    protected var mParticipantSelectedMap: HashMap<String, Boolean> = HashMap()
    protected var mPositionSpinnerPaidBy = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_spent)
        Icepick.restoreInstanceState(this, savedInstanceState)
        if (savedInstanceState != null){
            mPositionSpinnerPaidBy = savedInstanceState.getInt(POSITION_SPINNER_PAID_BY)
            mParticipantSelectedMap = savedInstanceState.getSerializable(IS_SELECTED_MAP) as HashMap<String, Boolean>
        }
        create_spent_title_editext.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkInformationIsEntered(0)
            }
        })
        create_spent_amount_editext.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkInformationIsEntered(1)
            }
        })
    }

    protected fun configureRecyclerView(){
        this.mAdapter = CreateSpentRecyclerViewAdapter(this)
        create_spent_who_recyclerview.adapter = this.mAdapter
        create_spent_who_recyclerview.layoutManager = LinearLayoutManager(this)
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

    private fun checkInformationIsEntered(signInCode: Int){
        val title = create_spent_title_editext.text.toString().isNotEmpty()
        val amount = create_spent_amount_editext.text.toString().isNotEmpty()
        create_spent_button.isEnabled = title && amount
        if (signInCode == 0){
            if (!title){
                create_spent_title_textInputLayout.error = getString(R.string.you_must_add_a_title)
            }else{
                create_spent_title_textInputLayout.error = null
            }
        }else if (signInCode == 1){
            if (!amount){
                create_spent_amount_textInputLayout.error = getString(R.string.you_must_add_an_amount)
            }else{
                create_spent_amount_textInputLayout.error = null
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Icepick.saveInstanceState(this, outState)
        outState.run {
            outState.putInt(POSITION_SPINNER_PAID_BY, mPositionSpinnerPaidBy)
            outState.putSerializable(IS_SELECTED_MAP, mParticipantSelectedMap)
        }
    }

    override fun onStop() {
        create_spent_payed_by_spinner.dismissDropDown()
        super.onStop()
    }
}