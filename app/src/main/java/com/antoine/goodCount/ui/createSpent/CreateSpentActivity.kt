package com.antoine.goodCount.ui.createSpent

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.antoine.goodCount.R
import com.antoine.goodCount.models.LineCommonPot
import com.antoine.goodCount.models.Participant
import com.antoine.goodCount.models.ParticipantSpent
import com.antoine.goodCount.ui.createSpent.recyclerView.ClickListener
import com.antoine.goodCount.ui.createSpent.recyclerView.CreateSpentRecyclerViewAdapter
import com.google.android.material.snackbar.Snackbar
import icepick.Icepick
import kotlinx.android.synthetic.main.activity_create_spent.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

private const val COMMON_POT_ID = "common pot id"
private const val POSITION_SPINNER_PAID_BY = "position spinner paid by"
private const val IS_SELECTED_MAP = "is selected map"
private const val ANSWER_WRITING_REQUEST = "answer writing request"
class CreateSpentActivity : AppCompatActivity(), ClickListener {

    private lateinit var mAdapter: CreateSpentRecyclerViewAdapter
    private lateinit var mCreateSpentViewModel: CreateSpentViewModel
    private lateinit var mCommonPotId: String
    private lateinit var mParticipantList: List<Participant>
    private var mParticipantSelectedMap: HashMap<String, Boolean> = HashMap()
    private var mPositionSpinnerPaidBy = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_spent)
        Icepick.restoreInstanceState(this, savedInstanceState)
        if (savedInstanceState != null){
            mPositionSpinnerPaidBy = savedInstanceState.getInt(POSITION_SPINNER_PAID_BY)
            mParticipantSelectedMap = savedInstanceState.getSerializable(IS_SELECTED_MAP) as HashMap<String, Boolean>
        }
        mCommonPotId = intent.getStringExtra(COMMON_POT_ID)
        this.configureViewModel()
        this.configureRecyclerView()
        this.getParticipant()
        this.configureDateTextView()
        create_spent_title_editext.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkInformationIsEntered(0)
            }
        })
        create_spent_amount_editext.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkInformationIsEntered(1)
            }
        })
        create_spent_button.setOnClickListener {
            this.retrieveInformationEntered()
        }
    }

    private fun configureViewModel(){
        mCreateSpentViewModel = ViewModelProviders.of(this).get(CreateSpentViewModel::class.java)
    }

    private fun configureRecyclerView(){
        this.mAdapter = CreateSpentRecyclerViewAdapter(this)
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

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
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
            if (mParticipantSelectedMap.isEmpty()) mParticipantSelectedMap = mCreateSpentViewModel.createMapParticipant(it)
            this.configureSpinner(mCreateSpentViewModel.createListUsername(it))
            this.mAdapter.updateData(it, mParticipantSelectedMap)
        })
    }

    private fun configureSpinner(usernameList: List<String>){
        create_spent_payed_by_spinner.keyListener = null
        create_spent_payed_by_spinner.text = SpannableStringBuilder(usernameList[mPositionSpinnerPaidBy])
        val adapter = ArrayAdapter(this, R.layout.dropdown_menu_popup_item, usernameList)
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

    override fun onClick(isChecked: Boolean, participantId: String) {
        mParticipantSelectedMap[participantId] = isChecked
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

    private fun retrieveInformationEntered(){
        val participantSpentList = ArrayList<ParticipantSpent>()
        if (mParticipantSelectedMap.containsValue(true)){
            val title = create_spent_title_editext.text.toString()
            val amount =  create_spent_amount_editext.text.toString().toDouble()
            val paidBy = mParticipantList[mPositionSpinnerPaidBy].id
            val lineCommonPot = LineCommonPot("",mCommonPotId,title,amount,mCreateSpentViewModel.mDateOfSpent,paidBy)
            for (select in mParticipantSelectedMap){
                participantSpentList.add(ParticipantSpent("","",select.key))
            }
            this.createSpentInDatabase(lineCommonPot, participantSpentList)
        }else{
            Snackbar.make(nestedScrollView, getString(R.string.you_must_allocate_the_amount_to_at_least_one_participant), Snackbar.LENGTH_LONG).show()
        }
    }

    private fun createSpentInDatabase(lineCommonPot: LineCommonPot, participantSpentList: List<ParticipantSpent>){
        val returnIntent = Intent()
        mCreateSpentViewModel.createSpentInDatabase(lineCommonPot, participantSpentList).addOnSuccessListener {
            returnIntent.putExtra(ANSWER_WRITING_REQUEST, 0)
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }.addOnFailureListener {
            returnIntent.putExtra(ANSWER_WRITING_REQUEST, 1)
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
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
