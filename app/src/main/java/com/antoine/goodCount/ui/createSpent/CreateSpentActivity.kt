package com.antoine.goodCount.ui.createSpent

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.antoine.goodCount.R
import com.antoine.goodCount.ui.createSpent.recyclerView.CreateSpentRecyclerViewAdapter
import icepick.Icepick
import kotlinx.android.synthetic.main.activity_create_spent.*
import java.text.DateFormat
import java.util.*

private const val COMMON_POT_ID = "common pot id"
private const val POSITION_SPINNER_PAID_BY = "position spinner paid by"
class CreateSpentActivity : AppCompatActivity() {

    private lateinit var mMenu: Menu
    private lateinit var mAdapter: CreateSpentRecyclerViewAdapter
    private lateinit var mCreateSpentViewModel: CreateSpentViewModel
    private lateinit var mCommonPotId: String
    private lateinit var mParticipantList: List<String>
    private var mPositionSpinnerPaidBy = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_spent)
        Icepick.restoreInstanceState(this, savedInstanceState)
        if (savedInstanceState != null) mPositionSpinnerPaidBy = savedInstanceState.getInt(POSITION_SPINNER_PAID_BY)
        mCommonPotId = intent.getStringExtra(COMMON_POT_ID)
        this.configureViewModel()
        this.configureRecyclerView()
        this.getParticipant()
        this.configureDateTextView()
    }

    private fun configureViewModel(){
        mCreateSpentViewModel = ViewModelProviders.of(this).get(CreateSpentViewModel::class.java)
    }

    private fun configureRecyclerView(){
        this.mAdapter = CreateSpentRecyclerViewAdapter()
        create_spent_who_recyclerview.adapter = this.mAdapter
        create_spent_who_recyclerview.layoutManager = LinearLayoutManager(this)
    }

    private fun configureDateTextView(){
        create_spent_date_spinner.keyListener = null
        create_spent_date_spinner.text = SpannableStringBuilder(mCreateSpentViewModel.formatDate())
        create_spent_date_spinner.setOnClickListener {
            create_spent_date_spinner.isFocusableInTouchMode = true
            create_spent_date_spinner.requestFocus()
            displayDatePicker()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar_activity_create, menu)
        menu.setGroupVisible(R.id.create_menu_group, false)
        mMenu = menu
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.create -> {
            this.createSpent()
            true
        }
        android.R.id.home -> {
            this.onBackPressed()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    private fun getParticipant(){
        mCreateSpentViewModel.getParticipantCommonPot(mCommonPotId).observe(this, Observer {
            mParticipantList = it
            this.configureSpinner()
            this.mAdapter.updateData(it)
        })
    }

    private fun configureSpinner(){
        create_spent_payed_by_spinner.keyListener = null
        create_spent_payed_by_spinner.text = SpannableStringBuilder(mParticipantList[mPositionSpinnerPaidBy])
        val adapter = ArrayAdapter(this, R.layout.dropdown_menu_popup_item, mParticipantList)
        create_spent_payed_by_spinner.setAdapter(adapter)
        create_spent_payed_by_spinner.setOnItemClickListener { _, _, position, _ ->
            mPositionSpinnerPaidBy = position
        }
    }

    private fun displayDatePicker(){
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, yearOfCalendar, monthOfYear, dayOfMonth ->
            calendar.set(yearOfCalendar, monthOfYear, dayOfMonth)
            this.displayTimePicker(calendar)
        }, year, month, day)
        dpd.show()
        dpd.setOnCancelListener {
            create_spent_date_spinner.isFocusableInTouchMode = false
            create_spent_date_spinner.requestFocus()
        }
    }

    private fun displayTimePicker(calendar: Calendar){
        val hour = calendar.get(Calendar.HOUR)
        val minute = calendar.get(Calendar.MINUTE)
        val tpd = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { _, hourOfDay, minuteOfHour ->
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minuteOfHour)
            mCreateSpentViewModel.mDateOfSpent = Date(calendar.timeInMillis)
            create_spent_date_spinner.text = SpannableStringBuilder(mCreateSpentViewModel.formatDate())
        }, hour, minute, true)
        tpd.show()
        tpd.setOnCancelListener {
            create_spent_date_spinner.isFocusableInTouchMode = false
            create_spent_date_spinner.requestFocus()
        }
    }

    private fun createSpent(){
        val title = create_spent_title_editext.text.toString()
        val amount =  create_spent_amount_editext.text.toString().toDouble()

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Icepick.saveInstanceState(this, outState)
        outState.run {
            outState.putInt(POSITION_SPINNER_PAID_BY, mPositionSpinnerPaidBy)
        }
    }

    override fun onStop() {
        create_spent_payed_by_spinner.dismissDropDown()
        super.onStop()
    }
}
