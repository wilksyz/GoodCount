package com.antoine.goodCount.ui.createSpent

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


        create_spent_date_spinner.setOnClickListener {
            Log.e("TAG", "Click")
        }
    }

    private fun configureViewModel(){
        mCreateSpentViewModel = ViewModelProviders.of(this).get(CreateSpentViewModel::class.java)
    }

    private fun configureRecyclerView(){
        this.mAdapter = CreateSpentRecyclerViewAdapter()
        create_spent_who_recyclerview.adapter = this.mAdapter
        create_spent_who_recyclerview.layoutManager = LinearLayoutManager(this)
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
        }else -> {
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
        create_spent_payed_by_spinner.text = SpannableStringBuilder(mParticipantList[mPositionSpinnerPaidBy])
        val adapter = ArrayAdapter(this, R.layout.dropdown_menu_popup_item, mParticipantList)
        create_spent_payed_by_spinner.setAdapter(adapter)
        create_spent_payed_by_spinner.setOnItemClickListener { _, _, position, _ ->
            mPositionSpinnerPaidBy = position
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
