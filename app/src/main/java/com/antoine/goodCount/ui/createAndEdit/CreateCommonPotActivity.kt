package com.antoine.goodCount.ui.createAndEdit

import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.antoine.goodCount.R
import com.antoine.goodCount.ui.createAndEdit.recyclerview.ClickListener
import com.antoine.goodCount.ui.createAndEdit.recyclerview.CreateRecyclerViewAdapter
import icepick.Icepick
import icepick.State
import kotlinx.android.synthetic.main.activity_create_common_pot.*

class CreateCommonPotActivity : AppCompatActivity(), ClickListener {

    private lateinit var mMenu: Menu
    private lateinit var mCreateViewModel: CreateViewModel
    private var mCurrencyList: List<String> = ArrayList()
    @State private var mPositionSpinner:Int = 0
    private val mContributorList : MutableList<String> = ArrayList()
    private lateinit var mAdapter: CreateRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_common_pot)
        Icepick.restoreInstanceState(this, savedInstanceState)
        if (savedInstanceState != null) mPositionSpinner = savedInstanceState.getInt("pos")
        this.configureViewModel()
        this.configureSpinner()
        this.configureRecyclerView()

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
        create_activity_add_participant_button.setOnClickListener {
            mContributorList.add(create_activity_contributor_editext.text.toString())
            mAdapter.updateData(mContributorList)
            create_activity_contributor_editext.text?.clear()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar_activity_create, menu)
        menu.setGroupVisible(R.id.create_menu_group, false)
        mMenu = menu
        return true
    }

    private fun configureViewModel(){
        mCreateViewModel = ViewModelProviders.of(this).get(CreateViewModel::class.java)
    }

    private fun configureSpinner(){
        mCurrencyList = mCreateViewModel.getAllCurrency()
        create_activity_spinner.text = SpannableStringBuilder(mCurrencyList[mPositionSpinner])
        val adapter = ArrayAdapter(this@CreateCommonPotActivity, R.layout.dropdown_menu_popup_item, mCurrencyList)
        create_activity_spinner.setAdapter(adapter)

        create_activity_spinner.setOnItemClickListener { parent, view, position, id ->
            Log.e("TAG","Passage $position")
            mPositionSpinner = position
        }
    }

    private fun configureRecyclerView(){
        this.mAdapter = CreateRecyclerViewAdapter(this)
        create_activity_recyclerview.adapter = this.mAdapter
        create_activity_recyclerview.layoutManager = LinearLayoutManager(this)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.create -> {
            this.createCommonPot()
            true
        }else -> {
            super.onOptionsItemSelected(item)
        }
    }

    private fun checkInformationIsEntered(){
        val title = create_activity_title_editext.toString().isNotEmpty()
        val name = create_activity_your_name_editext.toString().isNotEmpty()
        if (title && name){
            mMenu.setGroupVisible(R.id.create_menu_group, true)
        }
    }

    private fun createCommonPot(){

    }

    override fun onClick(position: Int) {
        mContributorList.removeAt(position)
        mAdapter.updateData(mContributorList)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Icepick.saveInstanceState(this, outState)
        outState.run {
            outState.putInt("pos", mPositionSpinner)
        }
    }

    override fun onStop() {
        create_activity_spinner.dismissDropDown()
        super.onStop()

    }
}
