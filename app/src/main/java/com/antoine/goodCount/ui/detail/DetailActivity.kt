package com.antoine.goodCount.ui.detail

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.antoine.goodCount.R
import com.antoine.goodCount.ui.BaseActivity
import com.antoine.goodCount.ui.detail.viewpager.SectionsPagerAdapter
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        this.configureViewPager()


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar_activity_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when(item.itemId) {
        R.id.disconnect_button -> {
            disconnect()
            true
        }
        R.id.share_button -> {

             true
        }
        R.id.edit_button -> {

             true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    private fun configureViewPager(){
        val sectionsPagerAdapter =
            SectionsPagerAdapter(this, supportFragmentManager)
        detail_activity_view_pager.adapter = sectionsPagerAdapter
        detail_activity_tablayout.setupWithViewPager(detail_activity_view_pager)
    }
}