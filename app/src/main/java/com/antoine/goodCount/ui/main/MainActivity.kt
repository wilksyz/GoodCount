package com.antoine.goodCount.ui.main

import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.antoine.goodCount.R
import com.antoine.goodCount.ui.BaseActivity

private const val USER = "user"
private const val USER_ID = "user id"
class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sharedPref: SharedPreferences = getSharedPreferences(USER, MODE_PRIVATE)
        val userId = sharedPref.getString(USER_ID, null)
        userId?.let { this.configureFragment(it) }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar_activity_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when(item.itemId) {
        R.id.disconnect_button -> {
            disconnect()
            true
        }else -> {
            super.onOptionsItemSelected(item)
        }
    }

    private fun configureFragment(userId: String){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val args = Bundle()
        args.putString(USER_ID, userId)
        val mainFragment = MainFragment()
        mainFragment.arguments = args
        fragmentTransaction.replace(R.id.fragment_main_container, mainFragment)
        fragmentTransaction.commit()
    }
}
