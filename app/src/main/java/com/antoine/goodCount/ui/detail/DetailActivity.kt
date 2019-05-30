package com.antoine.goodCount.ui.detail

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.antoine.goodCount.R
import com.antoine.goodCount.ui.detail.viewpager.SectionsPagerAdapter
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        this.configureViewPager()

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    private fun configureViewPager(){
        val sectionsPagerAdapter =
            SectionsPagerAdapter(this, supportFragmentManager)
        detail_activity_view_pager.adapter = sectionsPagerAdapter
        detail_activity_tablayout.setupWithViewPager(detail_activity_view_pager)
    }
}