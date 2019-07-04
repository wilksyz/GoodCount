package com.antoine.goodCount.ui.createAndEditSpent.edit

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
private const val LINE_COMMON_POT_ID = "line common pot id"
private const val ANSWER_WRITING_REQUEST = "answer writing request"
class EditSpentActivity: BaseSpentActivity() {

    private lateinit var mEditSpentViewModel: EditSpentViewModel
    private lateinit var mCommonPotId: String
    private lateinit var mLineCommonPotId: String
    private var mParticipantList: List<Participant> = ArrayList()
    private var mLineCommonPot: LineCommonPot? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mCommonPotId = intent.getStringExtra(COMMON_POT_ID)
        mLineCommonPotId = intent.getStringExtra(LINE_COMMON_POT_ID)
        this.configureViewModel()
        configureRecyclerView()
        this.getParticipant()
        this.getLineCommonPot()
        create_spent_button.setOnClickListener {
            this.retrieveInformationEntered()
        }
    }

    private fun configureViewModel(){
        mEditSpentViewModel = ViewModelProviders.of(this).get(EditSpentViewModel::class.java)
    }

    private fun getParticipant(){
        mEditSpentViewModel.getParticipantCommonPot(mCommonPotId).observe(this, Observer { participantList ->
            mParticipantList = participantList
            if (mParticipantSelectedMap.isEmpty()){
                mEditSpentViewModel.createParticipantMap(participantList, mLineCommonPotId).observe(this, Observer {
                    mParticipantSelectedMap = it
                    this.configureSpinner()
                    this.mAdapter.updateData(participantList, mParticipantSelectedMap)
                })
            }else {
                this.mAdapter.updateData(participantList, mParticipantSelectedMap)
            }
        })
    }

    private fun getLineCommonPot(){
        mEditSpentViewModel.getLineCommonPot(mLineCommonPotId).observe(this, Observer {lineCommonPot ->
            mLineCommonPot = lineCommonPot
            if (mEditSpentViewModel.mDateOfSpent == null) mEditSpentViewModel.mDateOfSpent = lineCommonPot.date
            this.configureDateTextView()
            this.configureSpinner()
            this.updateTitleAndAmount(lineCommonPot)
        })
    }

    private fun updateTitleAndAmount(lineCommonPot: LineCommonPot){
        if (create_spent_title_editext.text.toString().isEmpty()){
            create_spent_title_editext.text = SpannableStringBuilder(lineCommonPot.title)
        }
        if (create_spent_amount_editext.text.toString().isEmpty()){
            create_spent_amount_editext.text = SpannableStringBuilder(lineCommonPot.amount.toString())
        }
        create_spent_button.text = getString(R.string.validate_the_change)
    }

    private fun configureDateTextView(){
        create_spent_date_spinner.keyListener = null
        create_spent_date_spinner.text = SpannableStringBuilder(mEditSpentViewModel.formatDate())
        create_spent_date_spinner.setOnClickListener {
            create_spent_date_spinner.isFocusableInTouchMode = true
            create_spent_date_spinner.requestFocus()
            displayDatePicker()
        }
    }

    private fun configureSpinner(){
        if (mParticipantList.isNotEmpty() && mLineCommonPot != null){
            val usernameList: List<String> = User.createUsernameList(mParticipantList)
            if (mPositionSpinnerPaidBy == -1) mPositionSpinnerPaidBy = User.getPaidBy(usernameList, mParticipantList, mLineCommonPot)
            create_spent_payed_by_spinner.keyListener = null
            create_spent_payed_by_spinner.text = SpannableStringBuilder(usernameList[mPositionSpinnerPaidBy])
            val adapter = ArrayAdapter(this, R.layout.dropdown_menu_popup_item, usernameList)
            create_spent_payed_by_spinner.setAdapter(adapter)
            create_spent_payed_by_spinner.setOnItemClickListener { _, _, position, _ ->
                mPositionSpinnerPaidBy = position
            }
        }
    }

    private fun displayDatePicker(){
        val calendar = Calendar.getInstance()
        calendar.time = mEditSpentViewModel.mDateOfSpent
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
            mEditSpentViewModel.mDateOfSpent = Date(calendar.timeInMillis)
            create_spent_date_spinner.text = SpannableStringBuilder(mEditSpentViewModel.formatDate())
        }, hour, minute, true)
        tpd.show()
        tpd.setOnCancelListener {
            create_spent_date_spinner.isFocusableInTouchMode = false
            create_spent_date_spinner.requestFocus()
        }
    }

    private fun retrieveInformationEntered(){
        val participantSpentList = ArrayList<ParticipantSpent>()
        if (mParticipantSelectedMap.containsValue(true)){
            mLineCommonPot?.title = create_spent_title_editext.text.toString()
            mLineCommonPot?.amount = create_spent_amount_editext.text.toString().toDouble()
            mLineCommonPot?.paidBy = mParticipantList[mPositionSpinnerPaidBy].id
            mLineCommonPot?.date = mEditSpentViewModel.mDateOfSpent!!
            for (select in mParticipantSelectedMap){
                if (select.value) participantSpentList.add(ParticipantSpent("",mLineCommonPotId,select.key))
            }
            mLineCommonPot?.let { this.editSpentInDatabase(it, participantSpentList) }
        }else{
            Snackbar.make(nestedScrollView, getString(R.string.you_must_allocate_the_amount_to_at_least_one_participant), Snackbar.LENGTH_LONG).show()
        }
    }

    private fun editSpentInDatabase(lineCommonPot: LineCommonPot, participantSpentList: List<ParticipantSpent>){
        val returnIntent = Intent()
        mEditSpentViewModel.editSpentInDatabase(lineCommonPot, participantSpentList).addOnSuccessListener {
            returnIntent.putExtra(ANSWER_WRITING_REQUEST, 2)
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }.addOnFailureListener {
            returnIntent.putExtra(ANSWER_WRITING_REQUEST, 3)
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }
    }

    override fun onClick(isChecked: Boolean, participantId: String) {
        mParticipantSelectedMap[participantId] = isChecked
    }
}
