package com.antoine.goodCount.ui.detail

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.antoine.goodCount.R
import com.antoine.goodCount.ui.BaseActivity
import com.antoine.goodCount.ui.createAndEdit.edit.EditCommonPotActivity
import com.antoine.goodCount.ui.detail.viewpager.SectionsPagerAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_detail.*

private const val COMMON_POT_ID = "common pot id"
private const val ANSWER_REQUEST = 1918
private const val ANSWER_WRITING_REQUEST = "answer writing request"
class DetailActivity : BaseActivity() {

    private var mCommonPotId: String? = null
    private lateinit var mSectionsPagerAdapter: SectionsPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        supportActionBar?.elevation = 0F
        mCommonPotId = intent?.getStringExtra(COMMON_POT_ID)
    }

    override fun onResume() {
        super.onResume()
        mCommonPotId?.let { this.configureViewPager(it) }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar_activity_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when(item.itemId) {
        R.id.disconnect_button -> {
            mSectionsPagerAdapter.mSpentFragment.removeListener()
            mSectionsPagerAdapter.mBalanceFragment.removeListener()
            disconnect()
            true
        }
        R.id.share_button -> {
            mSectionsPagerAdapter.mSpentFragment.shareCommonPot()
            true
        }
        R.id.edit_button -> {
            val editCommonPotIntent = Intent(this, EditCommonPotActivity::class.java)
            editCommonPotIntent.putExtra(COMMON_POT_ID, mCommonPotId)
            startActivityForResult(editCommonPotIntent, ANSWER_REQUEST)
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ANSWER_REQUEST){
            if (resultCode == Activity.RESULT_OK){
                val code = data?.getIntExtra(ANSWER_WRITING_REQUEST, -1)
                if (code == 2 || code == 3){
                    this.commonPotIsDelete(code)
                }else{
                    code?.let { this.displaySnackBar(it) }
                }
            }
        }
    }

    private fun commonPotIsDelete(code: Int) {
        val returnIntent = Intent()
        returnIntent.putExtra(ANSWER_WRITING_REQUEST, code)
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }

    // Displays a message in a snackBar according to the return of activities
    private fun displaySnackBar(code: Int) {
        when(code){
            0 -> {
                Snackbar.make(detail_activity_constraintLayout, getString(R.string.successful_update), Snackbar.LENGTH_LONG).show()
            }
            1 -> {
                Snackbar.make(detail_activity_constraintLayout, getString(R.string.error_updating), Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun configureViewPager(commonPotId: String){
        mSectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager, commonPotId)
        detail_activity_view_pager.adapter = mSectionsPagerAdapter
        detail_activity_tablayout.setupWithViewPager(detail_activity_view_pager)
    }
}