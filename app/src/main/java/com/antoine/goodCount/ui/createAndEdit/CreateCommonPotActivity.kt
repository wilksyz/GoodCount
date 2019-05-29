package com.antoine.goodCount.ui.createAndEdit

import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.antoine.goodCount.R
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.antoine.goodCount.ui.createAndEdit.recyclerview.ClickListener
import com.antoine.goodCount.ui.createAndEdit.recyclerview.CreateRecyclerViewAdapter
import icepick.Icepick
import kotlinx.android.synthetic.main.activity_create_common_pot.*
import kotlin.collections.ArrayList

class CreateCommonPotActivity : AppCompatActivity(), ClickListener {

    private lateinit var mMenu: Menu
    private lateinit var mCreateViewModel: CreateViewModel
    private var mCurrencyList: List<String> = ArrayList()
    private val mContributorList : MutableList<String> = ArrayList()
    private lateinit var mAdapter: CreateRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_common_pot)
        Icepick.restoreInstanceState(this, savedInstanceState)
        this.configureViewModel()
        this.configureSpinner()
        this.configureRecyclerView()


        create_activity_contributor_editext.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                create_activity_add_participant_button.isEnabled = s.toString().isNotEmpty()
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
        create_activity_spinner.text = SpannableStringBuilder(mCurrencyList[12])
        val adapter = ArrayAdapter(this@CreateCommonPotActivity, R.layout.dropdown_menu_popup_item, mCurrencyList)
        create_activity_spinner.setAdapter(adapter)
        create_activity_spinner.setOnItemClickListener { parent, view, position, id ->

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

    private fun createCommonPot(){

    }

    override fun onClick(position: Int) {
        mContributorList.removeAt(position)
        mAdapter.updateData(mContributorList)
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        super.onSaveInstanceState(outState, outPersistentState)
        Icepick.saveInstanceState(this, outState)
    }
}
