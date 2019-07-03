package com.antoine.goodCount.ui.createAndEditSpent.create

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.antoine.goodCount.R
import com.antoine.goodCount.models.LineCommonPot
import com.antoine.goodCount.models.Participant
import com.antoine.goodCount.models.ParticipantSpent
import com.antoine.goodCount.ui.createAndEditSpent.BaseSpentActivity
import com.antoine.goodCount.utils.User
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_create_spent.*
import java.util.*
import kotlin.collections.ArrayList

private const val COMMON_POT_ID = "common pot id"
private const val ANSWER_WRITING_REQUEST = "answer writing request"
class CreateSpentActivity: BaseSpentActivity() {

    private lateinit var mCreateSpentViewModel: CreateSpentViewModel
    private lateinit var mCommonPotId: String
    private lateinit var mParticipantList: List<Participant>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mCommonPotId = intent.getStringExtra(COMMON_POT_ID)
        this.configureViewModel()
        configureRecyclerView()
        this.getParticipant()
        this.configureDateTextView()
        create_spent_button.setOnClickListener {
            this.retrieveInformationEntered()
        }
    }

    private fun configureViewModel(){
        mCreateSpentViewModel = ViewModelProviders.of(this).get(CreateSpentViewModel::class.java)
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

    private fun getParticipant(){
        mCreateSpentViewModel.getParticipantCommonPot(mCommonPotId).observe(this, Observer {
            mParticipantList = it
            if (mParticipantSelectedMap.isEmpty()) mParticipantSelectedMap = User.createMapParticipant(it)
            this.configureSpinner(User.createListUsername(it))
            this.mAdapter.updateData(it, mParticipantSelectedMap)
        })
    }

    private fun configureSpinner(usernameList: List<String>){
        if (mPositionSpinnerPaidBy == -1) mPositionSpinnerPaidBy = 0
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
        dpd.datePicker.maxDate = Date().time
        dpd.show()
        dpd.setOnCancelListener {
            create_spent_date_spinner.isFocusableInTouchMode = false
            create_spent_date_spinner.requestFocus()
        }
    }

    private fun displayTimePicker(calendar: Calendar){
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
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
}
